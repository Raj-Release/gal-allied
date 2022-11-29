/**
 * 
 */
package com.shaic.claim.rod.wizard.pages;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author ntv.vijayar
 *
 */
@Stateless
public class ReceiptOfDocumentService {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public ReceiptOfDocumentService() {
		super();
	}
	
	
}
