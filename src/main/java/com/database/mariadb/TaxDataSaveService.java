package com.database.mariadb;

import org.apache.commons.lang3.math.NumberUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaxDataSaveService {

    /**
     * 세금계산서 DB 저장
     * @param xmlFileList
     */
    public void saveTaxBill(List<Map<String, Object>> xmlFileList, String url, String name, String pw, String driver) {

        Map<String, String> persistenceMap = new HashMap<String, String>();

        persistenceMap.put("javax.persistence.jdbc.url", url);
        persistenceMap.put("javax.persistence.jdbc.user", name);
        persistenceMap.put("javax.persistence.jdbc.password", pw);
        persistenceMap.put("javax.persistence.jdbc.driver", driver);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("scraping", persistenceMap);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try{

            for(Map<String, Object> xmlFile : xmlFileList) {

                //[STEP-1] tax_invoice 데이터 저장
                Map<String, Object> exchMap = (Map<String, Object>) xmlFile.get("exch");
                Map<String, Object> taxInvMap = (Map<String, Object>) xmlFile.get("taxInv");
                Map<String, Object> taxInvSettMap = (Map<String, Object>) xmlFile.get("taxInvSett");

                TaxInvoice invoice = new TaxInvoice(
                        stringValueOf(taxInvMap.get("taxInvDocIssueId")),
                        stringValueOf(exchMap.get("exchDocId")),
                        stringValueOf(exchMap.get("exchDocReferId")),
                        stringValueOf(exchMap.get("exchDocIssueDateTime")),
                        stringValueOf(taxInvMap.get("taxInvDocIssueId")),
                        stringValueOf(taxInvMap.get("taxInvDocIssueDateTime")),
                        stringValueOf(taxInvMap.get("taxInvDocTypeCode")),
                        stringValueOf(taxInvMap.get("taxInvDocPurposeCode")),
                        stringValueOf(taxInvMap.get("taxInvDocAmendCode")),
                        stringValueOf(taxInvMap.get("taxInvDocOriId")),
                        stringValueOf(taxInvMap.get("taxInvDocDesc")),
                        stringValueOf(taxInvMap.get("taxInvDocRefId")),
                        stringValueOf(taxInvMap.get("taxInvDocAcceptStDateTime")),
                        stringValueOf(taxInvMap.get("taxInvDocAcceptEndDateTime")),
                        BigDecimal.valueOf(NumberUtils.toDouble(stringValueOf(taxInvMap.get("taxInvDocRefItemQuan")))),
                        stringValueOf(taxInvSettMap.get("invoicerId")),
                        stringValueOf(taxInvSettMap.get("invoicerTaxRegId")),
                        stringValueOf(taxInvSettMap.get("invoicerName")),
                        stringValueOf(taxInvSettMap.get("invoicerSpecifiedPerson")),
                        stringValueOf(taxInvSettMap.get("invoicerSpecifiedAddr")),
                        stringValueOf(taxInvSettMap.get("invoicerTypeCode")),
                        stringValueOf(taxInvSettMap.get("invoicerClassificationCode")),
                        stringValueOf(taxInvSettMap.get("invoicerDefineDept")),
                        stringValueOf(taxInvSettMap.get("invoicerDefinePerson")),
                        stringValueOf(taxInvSettMap.get("invoicerDefineTel")),
                        stringValueOf(taxInvSettMap.get("invoicerDefineUri")),
                        stringValueOf(taxInvSettMap.get("invoiceeId")),
                        stringValueOf(taxInvSettMap.get("invoiceeBusinessTypeCode")),
                        stringValueOf(taxInvSettMap.get("invoiceeTaxRegId")),
                        stringValueOf(taxInvSettMap.get("invoiceeName")),
                        stringValueOf(taxInvSettMap.get("invoiceeSpecifiedPerson")),
                        stringValueOf(taxInvSettMap.get("invoiceeSpecifiedAddr")),
                        stringValueOf(taxInvSettMap.get("invoiceeTypeCode")),
                        stringValueOf(taxInvSettMap.get("invoiceeClassificationCode")),
                        stringValueOf(taxInvSettMap.get("invoiceePriDept")),
                        stringValueOf(taxInvSettMap.get("invoiceePriPerson")),
                        stringValueOf(taxInvSettMap.get("invoiceePriTel")),
                        stringValueOf(taxInvSettMap.get("invoiceePriUri")),
                        stringValueOf(taxInvSettMap.get("invoiceeSecondDept")),
                        stringValueOf(taxInvSettMap.get("invoiceeSecondPerson")),
                        stringValueOf(taxInvSettMap.get("invoiceeSecondTel")),
                        stringValueOf(taxInvSettMap.get("invoiceeSecondUri")),
                        stringValueOf(taxInvSettMap.get("brokerId")),
                        stringValueOf(taxInvSettMap.get("brokerTaxRegId")),
                        stringValueOf(taxInvSettMap.get("brokerName")),
                        stringValueOf(taxInvSettMap.get("brokerSpecifiedPerson")),
                        stringValueOf(taxInvSettMap.get("brokerSpecifiedAddr")),
                        stringValueOf(taxInvSettMap.get("brokerTypeCode")),
                        stringValueOf(taxInvSettMap.get("brokerClassificationCode")),
                        stringValueOf(taxInvSettMap.get("brokerDefineDept")),
                        stringValueOf(taxInvSettMap.get("brokerDefinePerson")),
                        stringValueOf(taxInvSettMap.get("brokerDefineTel")),
                        stringValueOf(taxInvSettMap.get("brokerDefineUri")),
                        stringValueOf(taxInvSettMap.get("payTypeCode")),
                        BigDecimal.valueOf(NumberUtils.toDouble(stringValueOf(taxInvSettMap.get("payAmt")))),
                        BigDecimal.valueOf(NumberUtils.toDouble(stringValueOf(taxInvSettMap.get("chargeTotalAmt")))),
                        BigDecimal.valueOf(NumberUtils.toDouble(stringValueOf(taxInvSettMap.get("taxTotalAmt")))),
                        BigDecimal.valueOf(NumberUtils.toDouble(stringValueOf(taxInvSettMap.get("grandTotalAmt")))),
                        "scheduler",
                        LocalDateTime.now(),
                        "scheduler",
                        LocalDateTime.now()
                );

                em.persist(invoice);

                //[STEP-2] tax_invoice_item 데이터 저장
                List<Map<String, Object>> taxInvItemList = (List<Map<String, Object>>) xmlFile.get("taxInvItem");

                for(Map<String, Object> taxInvItem : taxInvItemList) {

                    TaxInvoiceItem item = new TaxInvoiceItem(
                            stringValueOf(taxInvMap.get("taxInvDocIssueId")),
                            stringValueOf(taxInvItem.get("seq")),
                            stringValueOf(taxInvItem.get("itemPurchExpDateTime")),
                            stringValueOf(taxInvItem.get("itemName")),
                            stringValueOf(taxInvItem.get("itemInform")),
                            stringValueOf(taxInvItem.get("itemDesc")),
                            BigDecimal.valueOf(NumberUtils.toDouble(stringValueOf(taxInvItem.get("itemChargeUnitQuan")))),
                            BigDecimal.valueOf(NumberUtils.toDouble(stringValueOf(taxInvItem.get("itemUnitAmt")))),
                            BigDecimal.valueOf(NumberUtils.toDouble(stringValueOf(taxInvItem.get("itemAmt")))),
                            BigDecimal.valueOf(NumberUtils.toDouble(stringValueOf(taxInvItem.get("itemCalcAmt")))),
                            "scheduler",
                            LocalDateTime.now(),
                            "scheduler",
                            LocalDateTime.now()
                    );

                    em.persist(item);
                }

            }

            //트랜잭션 커밋!
            tx.commit();

        }catch(Exception e){
            e.printStackTrace();
            tx.rollback();
        }finally{
            em.close();
        }
        emf.close();
    }

    private String stringValueOf(Object object) {
        return object == null ? "" : String.valueOf(object);
    }
}
