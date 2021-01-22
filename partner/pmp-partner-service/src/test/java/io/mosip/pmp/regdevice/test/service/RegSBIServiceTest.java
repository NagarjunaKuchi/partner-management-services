package io.mosip.pmp.regdevice.test.service;

import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.pmp.authdevice.dto.DeviceSearchDto;
import io.mosip.pmp.authdevice.dto.SBISearchDto;
import io.mosip.pmp.authdevice.dto.SecureBiometricInterfaceCreateDto;
import io.mosip.pmp.authdevice.dto.SecureBiometricInterfaceStatusUpdateDto;
import io.mosip.pmp.authdevice.dto.SecureBiometricInterfaceUpdateDto;
import io.mosip.pmp.authdevice.entity.DeviceDetail;
import io.mosip.pmp.regdevice.entity.RegDeviceDetail;
import io.mosip.pmp.regdevice.entity.RegSecureBiometricInterface;
import io.mosip.pmp.regdevice.entity.RegSecureBiometricInterfaceHistory;
import io.mosip.pmp.authdevice.exception.RequestException;
import io.mosip.pmp.regdevice.repository.RegDeviceDetailRepository;
import io.mosip.pmp.regdevice.repository.RegSecureBiometricInterfaceHistoryRepository;
import io.mosip.pmp.regdevice.repository.RegSecureBiometricInterfaceRepository;
import io.mosip.pmp.regdevice.service.RegSecureBiometricInterfaceService;
import io.mosip.pmp.regdevice.service.impl.RegSecureBiometricInterfaceServiceImpl;
import io.mosip.pmp.authdevice.util.AuditUtil;
import io.mosip.pmp.common.constant.Purpose;
import io.mosip.pmp.common.dto.Pagination;
import io.mosip.pmp.common.dto.SearchFilter;
import io.mosip.pmp.common.dto.SearchSort;
import io.mosip.pmp.common.helper.SearchHelper;
import io.mosip.pmp.common.util.PageUtils;
import io.mosip.pmp.common.validator.FilterColumnValidator;
import io.mosip.pmp.partner.PartnerserviceApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { PartnerserviceApplication.class })
@AutoConfigureMockMvc
@EnableWebMvc
public class RegSBIServiceTest {
	
	@Mock
	FilterColumnValidator filterColumnValidator;
	@Autowired
	private ObjectMapper objectMapper;
	@Mock
	PageUtils pageUtils;
	@Mock
	SearchHelper searchHelper;
	@InjectMocks
	RegSecureBiometricInterfaceService secureBiometricInterfaceService=new RegSecureBiometricInterfaceServiceImpl();
	@Mock
	RegDeviceDetailRepository deviceDetailRepository;
	@Mock
	AuditUtil auditUtil;
	@Mock
	RegSecureBiometricInterfaceRepository sbiRepository;
	@Mock
	RegSecureBiometricInterfaceHistoryRepository sbiHistoryRepository;
	
	private RequestWrapper<SBISearchDto> deviceRequestDto;
	RegDeviceDetail deviceDetail=new RegDeviceDetail();
	SecureBiometricInterfaceCreateDto sbicreatedto = new SecureBiometricInterfaceCreateDto();
	SecureBiometricInterfaceUpdateDto sbidto = new SecureBiometricInterfaceUpdateDto();
	RegSecureBiometricInterface secureBiometricInterface=new RegSecureBiometricInterface();
	RegSecureBiometricInterfaceHistory secureBiometricInterfaceHistory=new RegSecureBiometricInterfaceHistory();
	SearchFilter searchDto = new SearchFilter();
	SBISearchDto deviceSearchDetailDto = new SBISearchDto();
	SBISearchDto deviceSearchDetailDto1 = new SBISearchDto();
	DeviceSearchDto deviceSearchDto = new DeviceSearchDto();
	Pagination pagination = new Pagination();
	SearchSort searchSort = new SearchSort();
	SearchFilter searchFilter = new SearchFilter();
	@Before
	public void setup() {
		secureBiometricInterfaceHistory.setApprovalStatus("pending");
		secureBiometricInterfaceHistory.setDeviceDetailId("1234");
		secureBiometricInterfaceHistory.setSwBinaryHAsh("swb".getBytes());
		secureBiometricInterfaceHistory.setEffectDateTime(LocalDateTime.now());
		secureBiometricInterfaceHistory.setSwCreateDateTime(LocalDateTime.now());
		secureBiometricInterfaceHistory.setSwExpiryDateTime(LocalDateTime.now());
		secureBiometricInterfaceHistory.setActive(true);
		secureBiometricInterfaceHistory.setCrBy("110005");
		secureBiometricInterfaceHistory.setCrDtimes(LocalDateTime.now());
		secureBiometricInterfaceHistory.setUpdDtimes(LocalDateTime.now());
		secureBiometricInterfaceHistory.setUpdBy("110005");
		secureBiometricInterfaceHistory.setSwVersion("v1");
		secureBiometricInterfaceHistory.setId("1234");
		secureBiometricInterface.setApprovalStatus("pending");
		secureBiometricInterface.setDeviceDetailId("1234");
		secureBiometricInterface.setSwBinaryHash("swb".getBytes());
		secureBiometricInterface.setSwCreateDateTime(LocalDateTime.now());
		secureBiometricInterface.setSwExpiryDateTime(LocalDateTime.now());
		secureBiometricInterface.setActive(true);
		secureBiometricInterface.setCrBy("110005");
		secureBiometricInterface.setCrDtimes(LocalDateTime.now());
		secureBiometricInterface.setUpdDtimes(LocalDateTime.now());
		secureBiometricInterface.setUpdBy("110005");
		secureBiometricInterface.setSwVersion("v1");
		secureBiometricInterface.setId("1234");
		
		//Search
    	searchSort.setSortField("model");
    	searchSort.setSortType("asc");
    	searchFilter.setColumnName("model");
    	searchFilter.setFromValue("");
    	searchFilter.setToValue("");
    	searchFilter.setType("STARTSWITH");
    	searchFilter.setValue("b");
    	List<SearchSort> searchDtos1 = new ArrayList<SearchSort>();
    	searchDtos1.add(searchSort);
    	List<SearchFilter> searchfilterDtos = new ArrayList<SearchFilter>();
    	searchfilterDtos.add(searchFilter);
    	deviceSearchDetailDto1.setDeviceDetailId("1234");
    	deviceSearchDetailDto1.setFilters(searchfilterDtos);
    	deviceSearchDetailDto1.setSort(searchDtos1);
    	deviceSearchDetailDto.setDeviceDetailId("all");
    	deviceSearchDetailDto.setFilters(searchfilterDtos);
    	deviceSearchDetailDto.setSort(searchDtos1);
    	deviceSearchDto.setPurpose(Purpose.REGISTRATION);
    	pagination.setPageFetch(10);
    	pagination.setPageStart(0);
		
		sbidto.setDeviceDetailId("1234");
		sbidto.setSwBinaryHash("swb");
		sbidto.setSwCreateDateTime(LocalDateTime.now());
		sbidto.setSwExpiryDateTime(LocalDateTime.now());
		sbidto.setIsActive(true);
		sbidto.setIsItForRegistrationDevice(false);
		sbidto.setSwVersion("v1");
		sbidto.setId("1234");
		
		sbicreatedto.setDeviceDetailId("1234");
		sbicreatedto.setSwBinaryHash("swb");
		sbicreatedto.setSwCreateDateTime(LocalDateTime.now());
		sbicreatedto.setSwExpiryDateTime(LocalDateTime.now());
		
		sbicreatedto.setIsItForRegistrationDevice(false);
		sbicreatedto.setSwVersion("v1");
    	deviceDetail.setApprovalStatus("pending");
    	deviceDetail.setDeviceProviderId("1234");
    	deviceDetail.setDeviceSubTypeCode("123");
    	deviceDetail.setDeviceTypeCode("123");
    	deviceDetail.setId("121");
    	deviceDetail.setIsActive(true);
    	deviceDetail.setCrBy("110005");
    	deviceDetail.setUpdBy("110005");
    	deviceDetail.setCrDtimes(LocalDateTime.now());
    	deviceDetail.setUpdDtimes(LocalDateTime.now());
    	deviceDetail.setMake("make");
    	deviceDetail.setModel("model");
    	deviceDetail.setPartnerOrganizationName("pog");
		Mockito.doNothing().when(auditUtil).auditRequest(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
		Mockito.doNothing().when(auditUtil).auditRequest(Mockito.any(), Mockito.any(), Mockito.any());
		Mockito.doReturn(secureBiometricInterface).when(sbiRepository).findByIdAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString());
		Mockito.doReturn(secureBiometricInterface).when(sbiRepository).save(Mockito.any());
		Mockito.doReturn(secureBiometricInterfaceHistory).when(sbiHistoryRepository).save(Mockito.any());
		
		Mockito.doReturn(deviceDetail).when(deviceDetailRepository).findByIdAndIsDeletedFalseOrIsDeletedIsNullAndIsActiveTrue(Mockito.anyString());
		
		}
	
	@Test
	public void regSBISearchTest() throws Exception{
		objectMapper.writeValueAsString(deviceRequestDto);
		DeviceDetail device = new DeviceDetail();
		device.setId("1001");
		Mockito.doReturn(new PageImpl<>(Arrays.asList(device))).when(searchHelper).search(Mockito.any(),Mockito.any(),Mockito.any());
		secureBiometricInterfaceService.searchSecureBiometricInterface(RegSecureBiometricInterface.class, deviceSearchDetailDto);
	}
	
	@Test
	public void regSBISearchTest1() throws Exception{
		objectMapper.writeValueAsString(deviceRequestDto);
		DeviceDetail device = new DeviceDetail();
		device.setId("1001");
		Mockito.doReturn(new PageImpl<>(Arrays.asList(device))).when(searchHelper).search(Mockito.any(),Mockito.any(),Mockito.any());
		secureBiometricInterfaceService.searchSecureBiometricInterface(RegSecureBiometricInterface.class, deviceSearchDetailDto1);
	}
	
	@Test
    public void createSBITest() throws Exception {
		assertTrue(secureBiometricInterfaceService.createSecureBiometricInterface(sbicreatedto).getId().equals("1234"));
    }
	
	@Test(expected=RequestException.class)
    public void createSBINoDviceTest() throws Exception {
		Mockito.doReturn(null).when(deviceDetailRepository).findByIdAndIsDeletedFalseOrIsDeletedIsNullAndIsActiveTrue(Mockito.anyString());
		
		secureBiometricInterfaceService.createSecureBiometricInterface(sbicreatedto);
    }
	
	@Test(expected=RequestException.class)
    public void updateSBINoDeviceTest() throws Exception {
		Mockito.doReturn(null).when(deviceDetailRepository).findByIdAndIsDeletedFalseOrIsDeletedIsNullAndIsActiveTrue(Mockito.anyString());
		
		secureBiometricInterfaceService.updateSecureBiometricInterface(sbidto);
    }
	

	@Test
    public void updateDeviceDetailTest() throws Exception {
		assertTrue(secureBiometricInterfaceService.updateSecureBiometricInterface(sbidto).getId().equals("1234"));
    }
	@Test(expected=RequestException.class)
    public void updateDeviceDetailNotFoundTest() throws Exception {
		Mockito.doReturn(null).when(sbiRepository).findByIdAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString());
		secureBiometricInterfaceService.updateSecureBiometricInterface(sbidto);
    }
	
	@Test
	public void updateDeviceDetailStatusTest_Approve() {
		secureBiometricInterfaceService.updateSecureBiometricInterfaceStatus(statusUpdateRequest("Activate"));
	}
	
	@Test
	public void updateDeviceDetailStatusTest_Reject() {
		secureBiometricInterfaceService.updateSecureBiometricInterfaceStatus(statusUpdateRequest("De-activate"));
	}
	
	@Test(expected = RequestException.class)
	public void updateDeviceDetailStatusTest_Status_Exception() {
		secureBiometricInterfaceService.updateSecureBiometricInterfaceStatus(statusUpdateRequest("De-Activate"));
	}
	
	@Test(expected = RequestException.class)
	public void updateDeviceDetailStatusTest_DeviceDetail_Exception() {
		SecureBiometricInterfaceStatusUpdateDto request = statusUpdateRequest("De-Activate");
		request.setId("34567");
		Mockito.doReturn(null).when(sbiRepository).findByIdAndIsDeletedFalseOrIsDeletedIsNull(Mockito.anyString());
		secureBiometricInterfaceService.updateSecureBiometricInterfaceStatus(request);
	}
	
	private SecureBiometricInterfaceStatusUpdateDto statusUpdateRequest(String status) {
		SecureBiometricInterfaceStatusUpdateDto request = new SecureBiometricInterfaceStatusUpdateDto();
		request.setApprovalStatus(status);
		request.setId("121");
		return request;
	}
}
