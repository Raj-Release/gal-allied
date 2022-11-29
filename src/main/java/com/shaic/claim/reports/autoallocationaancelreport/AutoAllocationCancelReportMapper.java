package com.shaic.claim.reports.autoallocationaancelreport;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.AutoAllocationCancelRemarks;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class AutoAllocationCancelReportMapper {

	private static MapperFacade tableMapper;
	static AutoAllocationCancelReportMapper myObj;
	private static MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	private static ClassMapBuilder<AutoAllocationCancelRemarks, AutoAllocationCancelDetailReportDTO> classMapForCancelReport = null;

	public static void getAllMapValues() {

		classMapForCancelReport = mapperFactory.classMap(AutoAllocationCancelRemarks.class,AutoAllocationCancelDetailReportDTO.class);

		classMapForCancelReport.field("intimationKey", "intimationKey");
		classMapForCancelReport.field("cancelRemarks", "cancelRemarks");
		classMapForCancelReport.field("cancelledBy", "cancelledBy");
		classMapForCancelReport.field("cancelledDate", "cancelledDate");
		classMapForCancelReport.register();
		tableMapper = mapperFactory.getMapperFacade();

	}

	public static List<AutoAllocationCancelDetailReportDTO> getcancelRemarksTableObjects(List<AutoAllocationCancelRemarks> autoAllocationCancelRemarksList) {
		List<AutoAllocationCancelDetailReportDTO> cancelTableObjectList = tableMapper.mapAsList(autoAllocationCancelRemarksList,AutoAllocationCancelDetailReportDTO.class);
		return cancelTableObjectList;
	}

	public static AutoAllocationCancelReportMapper getInstance() {
		if (myObj == null) {
			myObj = new AutoAllocationCancelReportMapper();
			getAllMapValues();
		}
		return myObj;
	}

}
