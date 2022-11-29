/**
 * 
 */
package com.shaic.claim.fss.searchfile;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.fss.filedetail.ChequeDetailsTableDTO;
import com.shaic.domain.fss.ChequeDetails;
import com.shaic.domain.fss.FileStorage;

/**
 * 
 *
 */
public class SearchDataEntryMapper {
	
	static MapperFactory mapperFactory = new DefaultMapperFactory.Builder().compilerStrategy(new EclipseJdtCompilerStrategy()).build();

	static SearchDataEntryMapper  myObj;
	
	private static MapperFacade tableMapper;
	private static MapperFacade chequeDetailsMapper;
	
	private static ClassMapBuilder<FileStorage, SearchDataEntryTableDTO> dataEntryMap = mapperFactory
			.classMap(FileStorage.class, SearchDataEntryTableDTO.class);
	
	private static ClassMapBuilder<ChequeDetails, ChequeDetailsTableDTO> chequeDetailMap = mapperFactory
			.classMap(ChequeDetails.class, ChequeDetailsTableDTO.class);
		
	public static void getAllMapValues() {

		dataEntryMap.field("key", "key");
		dataEntryMap.field("claimId", "claimNo");
		dataEntryMap.field("patientName", "patientName");
		dataEntryMap.field("storage.storageDesc", "location");
		
		dataEntryMap.field("storage.key", "selectLocation.id");
		dataEntryMap.field("storage.storageDesc", "selectLocation.value");
		
		dataEntryMap.field("rack.rackDesc", "rackNo");
		
		dataEntryMap.field("rack.key", "selectRack.id");
		dataEntryMap.field("rack.rackDesc", "selectRack.value");
		
		dataEntryMap.field("shelf.shelfDesc", "shelfNo");
		
		dataEntryMap.field("shelf.key", "selectShelf.id");
		dataEntryMap.field("shelf.shelfDesc", "selectShelf.value");
		
		dataEntryMap.field("inOutFlag", "checkInOutStatus");
		dataEntryMap.field("client.clientDesc", "client");
		dataEntryMap.field("year", "year");
		dataEntryMap.field("almirahNo", "almirahNo");
		dataEntryMap.field("bundleNo", "bundleNo");
		dataEntryMap.field("rejectFlag", "rejectStatus");

		dataEntryMap.register();
		
		chequeDetailMap.field("key", "key");
		chequeDetailMap.field("chequeNo", "chequeNo");
		chequeDetailMap.field("chequeDate", "chequeDate");
		chequeDetailMap.field("bankName", "bankName");
		chequeDetailMap.field("bankBranch", "bankBranch");

		chequeDetailMap.register();
		
		tableMapper = mapperFactory.getMapperFacade();
		chequeDetailsMapper = mapperFactory.getMapperFacade();

	}


	@SuppressWarnings("unused")
	public  List<SearchDataEntryTableDTO> getDataEntryList(
			List<FileStorage> list) {
		List<SearchDataEntryTableDTO> mapAsList = tableMapper
				.mapAsList(list,
						SearchDataEntryTableDTO.class);
		return mapAsList;
	}
	
	@SuppressWarnings("unused")
	public  List<ChequeDetailsTableDTO> getChequeList(
			List<ChequeDetails> list) {
		List<ChequeDetailsTableDTO> mapAsList = chequeDetailsMapper
				.mapAsList(list,
						ChequeDetailsTableDTO.class);
		return mapAsList;
	}
	
	public static SearchDataEntryMapper getInstance(){
        if(myObj == null){
            myObj = new SearchDataEntryMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}