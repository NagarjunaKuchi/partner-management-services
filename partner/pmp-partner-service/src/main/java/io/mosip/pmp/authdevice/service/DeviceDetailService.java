package io.mosip.pmp.authdevice.service;

import org.springframework.stereotype.Service;

import io.mosip.pmp.authdevice.dto.DeviceDetailDto;
import io.mosip.pmp.authdevice.dto.DeviceDetailSearchResponseDto;
import io.mosip.pmp.authdevice.dto.DeviceDetailUpdateDto;
import io.mosip.pmp.authdevice.dto.DeviceSearchDto;
import io.mosip.pmp.authdevice.dto.FilterResponseCodeDto;
import io.mosip.pmp.authdevice.dto.IdDto;
import io.mosip.pmp.authdevice.dto.RegistrationSubTypeDto;
import io.mosip.pmp.authdevice.dto.UpdateDeviceDetailStatusDto;
import io.mosip.pmp.authdevice.exception.AuthDeviceServiceException;
import io.mosip.pmp.common.dto.DeviceFilterValueDto;
import io.mosip.pmp.common.dto.PageResponseDto;

@Service
public interface DeviceDetailService {
	/**
	 * Function to save Device  Details to the Database
	 * 
	 * @param deviceDetails input from user deviceDetails DTO
	 * 
	 * @return IdResponseDto Device Details ID which is successfully inserted
	 * @throws AuthDeviceServiceException if any error occurred while saving device
	 *                                    Specification
	 */
	public IdDto createDeviceDetails(DeviceDetailDto deviceDetails);

	/**
	 * Function to update Device Details
	 * 
	 * @param deviceDetails input from user deviceDetails DTO
	 * 
	 * @return IdResponseDto Device Details ID which is successfully updated
	 * @throws AuthDeviceServiceException if any error occurred while updating
	 *                                    device Specification
	 */

	public IdDto updateDeviceDetails(DeviceDetailUpdateDto deviceDetails);
	
	/**
	 * Function to approve/reject device details
	 * 
	 * @param deviceDetails
	 * @return 
	 */
	public String updateDeviceDetailStatus(UpdateDeviceDetailStatusDto deviceDetails);
	
	/**
	 * 
	 * @param <E>
	 * @param entity
	 * @param dto
	 * @return
	 */
	public <E> PageResponseDto<DeviceDetailSearchResponseDto> searchDeviceDetails(Class<E> entity, DeviceSearchDto dto);
	
	/**
	 * 
	 * @param <E>
	 * @param entity
	 * @param dto
	 * @return
	 */
	public <E> PageResponseDto<RegistrationSubTypeDto> searchDeviceType(Class<E> entity, DeviceSearchDto dto);
	
	/**
	 * 
	 * @param deviceFilterValueDto
	 * @return
	 */
	public FilterResponseCodeDto deviceFilterValues(DeviceFilterValueDto deviceFilterValueDto);
	
	/**
	 * 
	 * @param deviceFilterValueDto
	 * @return
	 */
	public FilterResponseCodeDto deviceSubTypeFilterValues(DeviceFilterValueDto deviceFilterValueDto);
	
	/**
	 * 
	 * @param deviceFilterValueDto
	 * @return
	 */
	public FilterResponseCodeDto deviceTypeFilterValues(DeviceFilterValueDto deviceFilterValueDto);



}
