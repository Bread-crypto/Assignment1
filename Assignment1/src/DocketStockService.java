import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/** Story 13/24: issues stock for a generated docket in one transaction.
 * One generated DeliveryItem represents one copy for one customer.
 */
public class DocketStockService {
    public static class IssueResult {
        private final int issuedLines; private final List<String> outOfStock;
        IssueResult(int issuedLines,List<String> outOfStock){this.issuedLines=issuedLines;this.outOfStock=outOfStock;}
        public int getIssuedLines(){return issuedLines;} public List<String> getOutOfStock(){return Collections.unmodifiableList(outOfStock);}
        public boolean isSuccessful(){return outOfStock.isEmpty();}
    }
    public IssueResult issueDocket(int docketId, LocalDate date, List<DeliveryDocketGenerator.DeliveryItem> items) throws SQLException {
        if(docketId<=0 || date==null || items==null) throw new IllegalArgumentException("Docket, date and delivery items are required.");
        Map<Integer,Integer> totals=new LinkedHashMap<>(); Map<Integer,Publication> pubs=new HashMap<>();
        for(DeliveryDocketGenerator.DeliveryItem item:items){ if(item==null||item.getCustomer()==null||item.getPublication()==null)continue; int id=item.getPublication().getPublicationId(); totals.put(id,totals.getOrDefault(id,0)+1); pubs.put(id,item.getPublication()); }
        Map<Integer,Boolean> available=new HashMap<>(); List<String> unavailable=new ArrayList<>();
        try(Connection c=DatabaseConnection.getConnection()){
            c.setAutoCommit(false);
            try {
                // Lock and assess each publication independently: an out-of-stock title
                // must not prevent other available titles from being issued.
                for(Map.Entry<Integer,Integer> e:totals.entrySet()){
                    try(PreparedStatement ps=c.prepareStatement("SELECT publicationName,stockQuantity FROM publication WHERE publicationId=? FOR UPDATE")){
                        ps.setInt(1,e.getKey()); try(ResultSet rs=ps.executeQuery()){
                            if(!rs.next()){available.put(e.getKey(),false); unavailable.add(pubs.get(e.getKey()).getPublicationName()+" (publication missing)");}
                            else {boolean ok=rs.getInt("stockQuantity")>=e.getValue(); available.put(e.getKey(),ok); if(!ok) unavailable.add(rs.getString("publicationName")+" (required "+e.getValue()+", available "+rs.getInt("stockQuantity")+")");}
                        }
                    }
                }
                StockHistoryDAO history=new StockHistoryDAO();
                for(Map.Entry<Integer,Integer> e:totals.entrySet()) if(Boolean.TRUE.equals(available.get(e.getKey()))){
                    int before; try(PreparedStatement ps=c.prepareStatement("SELECT stockQuantity FROM publication WHERE publicationId=? FOR UPDATE")){ps.setInt(1,e.getKey());try(ResultSet rs=ps.executeQuery()){rs.next();before=rs.getInt(1);}}
                    int after=before-e.getValue(); try(PreparedStatement ps=c.prepareStatement("UPDATE publication SET stockQuantity=? WHERE publicationId=?")){ps.setInt(1,after);ps.setInt(2,e.getKey());ps.executeUpdate();}
                    history.record(c,e.getKey(),"ISSUED",e.getValue(),before,after,docketId,"Issued for docket "+docketId);
                }

String line = "INSERT INTO DeliveryDocketLineTable "
        + "(docketId, orderId, customerId, publicationId, "
        + "deliveryDate, quantity, deliveryStatus) "
        + "VALUES (?, ?, ?, ?, ?, 1, ?)";

try (PreparedStatement ps = c.prepareStatement(line)) {
    for (DeliveryDocketGenerator.DeliveryItem item : items) {
        if (item == null || item.getCustomer() == null
                || item.getPublication() == null || item.getOrder() == null) {
            continue;
        }

        ps.setInt(1, docketId);
        ps.setInt(2, item.getOrder().getOrderId());
        ps.setInt(3, item.getCustomer().getCustomerId());
        ps.setInt(4, item.getPublication().getPublicationId());
        ps.setDate(5, java.sql.Date.valueOf(date));
        ps.setString(6, Boolean.TRUE.equals(
                available.get(item.getPublication().getPublicationId()))
                ? "PENDING" : "OUT OF STOCK");

        ps.addBatch();
    }
    ps.executeBatch();
}

                c.commit(); int issuedLines=0; for(DeliveryDocketGenerator.DeliveryItem item:items) if(item!=null && item.getPublication()!=null && Boolean.TRUE.equals(available.get(item.getPublication().getPublicationId()))) issuedLines++;
                return new IssueResult(issuedLines,unavailable);
            } catch(SQLException|RuntimeException ex){c.rollback();throw ex;} finally {c.setAutoCommit(true);}
        }
    }
    /** Story 24: customers whose docket lines could not be issued for stock reasons. */
    public List<String> getOutOfStockCustomers(LocalDate date) throws SQLException {
        List<String> rows=new ArrayList<>(); 
        String sql = "SELECT DISTINCT c.customerId, c.firstName, c.surname, p.publicationName "
                + "FROM ("
                + "SELECT customerId, publicationId, deliveryDate "
                + "FROM DeliveryDocketLineTable WHERE deliveryStatus = 'OUT OF STOCK' "
                + "UNION ALL "
                + "SELECT customerId, publicationId, deliveryDate "
                + "FROM DeliveryTable WHERE deliveryStatus = 'OUT OF STOCK'"
                + ") dl "
                + "JOIN CustomerTable c ON c.customerId = dl.customerId "
                + "JOIN publication p ON p.publicationId = dl.publicationId "
                + "WHERE dl.deliveryDate = ? "
                + "ORDER BY c.surname, c.firstName";

        try(Connection c=DatabaseConnection.getConnection();PreparedStatement ps=c.prepareStatement(sql)){ps.setDate(1,java.sql.Date.valueOf(date));try(ResultSet rs=ps.executeQuery()){while(rs.next())rows.add(rs.getInt(1)+" - "+rs.getString(2)+" "+rs.getString(3)+" ("+rs.getString(4)+")");}} return rows;
    }
    public boolean returnUndelivered(int docketLineId) throws SQLException {
        try(Connection c=DatabaseConnection.getConnection()){c.setAutoCommit(false);try{int pubId,qty;try(PreparedStatement ps=c.prepareStatement("SELECT publicationId,quantity FROM DeliveryDocketLineTable WHERE docketLineId=? AND deliveryStatus IN ('PENDING','MISSED') FOR UPDATE")){ps.setInt(1,docketLineId);try(ResultSet rs=ps.executeQuery()){if(!rs.next()){c.rollback();return false;}pubId=rs.getInt(1);qty=rs.getInt(2);}}int before;try(PreparedStatement ps=c.prepareStatement("SELECT stockQuantity FROM publication WHERE publicationId=? FOR UPDATE")){ps.setInt(1,pubId);try(ResultSet rs=ps.executeQuery()){rs.next();before=rs.getInt(1);}}try(PreparedStatement ps=c.prepareStatement("UPDATE publication SET stockQuantity=? WHERE publicationId=?")){ps.setInt(1,before+qty);ps.setInt(2,pubId);ps.executeUpdate();}new StockHistoryDAO().record(c,pubId,"RETURNED",qty,before,before+qty,null,"Returned undelivered docket line "+docketLineId);try(PreparedStatement ps=c.prepareStatement("UPDATE DeliveryDocketLineTable SET deliveryStatus='RETURNED' WHERE docketLineId=?")){ps.setInt(1,docketLineId);ps.executeUpdate();}c.commit();return true;}catch(SQLException|RuntimeException ex){c.rollback();throw ex;}finally{c.setAutoCommit(true);}}
    }
}
