package com.shaic.claim.intimation.create.dto;

import java.util.ArrayList;
import java.util.List;

import com.shaic.claim.ClaimDto;

public class DTODataSourceFactory {
    
    private static List<ClaimDto> claimDtoList = new ArrayList<ClaimDto>();

    public DTODataSourceFactory()
    {
      super();
        
    }

    public static void setClaimDtoList(List<ClaimDto> claimDtoList) {
        DTODataSourceFactory.claimDtoList = claimDtoList;
    }

    public static List<ClaimDto> getClaimDtoList() {
        return getClaimDtos();
    }
    
    public static List<ClaimDto> getClaimDtos()
    {
      return claimDtoList;    
    }
}
