package io.mosip.pmp.policy.test.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import io.mosip.pmp.common.dto.FilterDto;
import io.mosip.pmp.common.dto.FilterValueDto;
import io.mosip.pmp.common.dto.PageResponseDto;
import io.mosip.pmp.common.dto.Pagination;
import io.mosip.pmp.common.dto.PolicyFilterValueDto;
import io.mosip.pmp.common.dto.PolicySearchDto;
import io.mosip.pmp.common.dto.SearchAuthPolicy;
import io.mosip.pmp.common.dto.SearchDto;
import io.mosip.pmp.common.dto.SearchFilter;
import io.mosip.pmp.common.dto.SearchSort;
import io.mosip.pmp.common.entity.PolicyGroup;
import io.mosip.pmp.common.validator.FilterColumnValidator;
import io.mosip.pmp.policy.dto.AuthPolicyDto;
import io.mosip.pmp.policy.dto.FilterResponseCodeDto;
import io.mosip.pmp.policy.dto.KeyValuePair;
import io.mosip.pmp.policy.dto.PolicyCreateRequestDto;
import io.mosip.pmp.policy.dto.PolicyCreateResponseDto;
import io.mosip.pmp.policy.dto.PolicyGroupCreateRequestDto;
import io.mosip.pmp.policy.dto.PolicyGroupCreateResponseDto;
import io.mosip.pmp.policy.dto.PolicyGroupUpdateRequestDto;
import io.mosip.pmp.policy.dto.PolicyResponseDto;
import io.mosip.pmp.policy.dto.PolicyStatusUpdateRequestDto;
import io.mosip.pmp.policy.dto.PolicyStatusUpdateResponseDto;
import io.mosip.pmp.policy.dto.PolicyUpdateRequestDto;
import io.mosip.pmp.policy.dto.PolicyWithAuthPolicyDto;
import io.mosip.pmp.policy.dto.RequestWrapper;
import io.mosip.pmp.policy.dto.ResponseWrapper;
import io.mosip.pmp.policy.dto.ShareableAttributesDto;
import io.mosip.pmp.policy.errorMessages.PolicyManagementServiceException;
import io.mosip.pmp.policy.service.PolicyManagementService;
import io.mosip.pmp.policy.test.PolicyServiceTest;
import io.mosip.pmp.policy.util.AuditUtil;


/**
 * @author Nagarjuna Kuchi
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PolicyServiceTest.class)
@AutoConfigureMockMvc
@EnableWebMvc
public class PolicyManagementControllerTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@MockBean
	private PolicyManagementService policyManagementService;
	
	@Autowired
	private ObjectMapper objectMapper;	
	
	@Mock
	FilterColumnValidator filterColumnValidator;
	
	@MockBean
	private AuditUtil audit;
	
	
	FilterDto filterDto = new FilterDto();
	SearchFilter searchFilter = new SearchFilter();
	FilterValueDto filterValueDto = new FilterValueDto();
	PolicyFilterValueDto policyFilterValueDto = new PolicyFilterValueDto();
	PolicySearchDto policySearchDto = new PolicySearchDto();
	Pagination pagination = new Pagination();
	SearchSort searchSort = new SearchSort();
	SearchDto searchDto = new SearchDto();
	@Before
	public void setUp() {
		ReflectionTestUtils.setField(policyManagementService, "filterColumnValidator", filterColumnValidator);
		Mockito.doNothing().when(audit).setAuditRequestDto(Mockito.any());
		filterDto.setColumnName("name");
		filterDto.setText("");
		filterDto.setType("ALL");
		searchFilter.setColumnName("name");
		searchFilter.setFromValue("");
		searchFilter.setToValue("");
		searchFilter.setType("ALL");
		searchFilter.setValue("m");
		List<FilterDto> filterDtos = new ArrayList<FilterDto>();
    	filterDtos.add(filterDto);
    	List<SearchFilter> searchDtos = new ArrayList<SearchFilter>();
    	searchDtos.add(searchFilter);
    	filterValueDto.setFilters(filterDtos);
    	filterValueDto.setOptionalFilters(searchDtos);
    	policyFilterValueDto.setPolicyType("");
    	policyFilterValueDto.setFilters(filterDtos);
    	policyFilterValueDto.setOptionalFilters(searchDtos);
    	policySearchDto.setPolicyType("");
    	searchDto.setFilters(searchDtos);
    	searchSort.setSortField("model");
    	searchSort.setSortType("asc");
    	List<SearchSort> searchDtos1 = new ArrayList<SearchSort>();
    	searchDtos1.add(searchSort);
    	searchDto.setSort(searchDtos1);
    	pagination.setPageFetch(10);
    	pagination.setPageStart(0);
    	
	}
	
	@Test
	@WithMockUser(roles = {"POLICYMANAGER"})
	public void getValueForKeyTest() throws PolicyManagementServiceException, Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/policies/key/12345")).
		andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(roles = {"POLICYMANAGER"})
	public void getPolicyGroups() throws PolicyManagementServiceException, Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/policies/policyGroups")).
		andExpect(MockMvcResultMatchers.status().isOk());
	}
		
	@Test
	@WithMockUser(roles = {"POLICYMANAGER"})
	public void getPolicyGroupTest() throws Exception{
		PolicyWithAuthPolicyDto response = new PolicyWithAuthPolicyDto();
		Mockito.when(policyManagementService.getPolicyGroupPolicy(Mockito.any())).thenReturn(response);
		mockMvc.perform(MockMvcRequestBuilders.get("/policies/policyGroupId/12345")).
		andExpect(MockMvcResultMatchers.status().isOk());		
	}
	
	@Test
	@WithMockUser(roles = {"POLICYMANAGER"})
	public void getPartnersPolicyTest() throws Exception{
		PolicyResponseDto response = new PolicyResponseDto();
		Mockito.when(policyManagementService.getPartnerMappedPolicy(Mockito.any(),Mockito.any())).thenReturn(response);
		mockMvc.perform(MockMvcRequestBuilders.get("/policies/partnerId/12345/policyId/12345")).
		andExpect(MockMvcResultMatchers.status().isOk());		
	}
	
	@Test
	@WithMockUser(roles = {"POLICYMANAGER"})
	public void publishPolicyTest() throws Exception{
		PolicyResponseDto response = new PolicyResponseDto();
		Mockito.when(policyManagementService.publishPolicy(Mockito.any(),Mockito.any())).thenReturn(response);
		mockMvc.perform(MockMvcRequestBuilders.post("/policies/publishPolicy/policyGroupId/12345/policyId/12345")).
		andExpect(MockMvcResultMatchers.status().isOk());		
	}
	
	@Test
	@WithMockUser(roles = {"POLICYMANAGER"})
	public void searchPolicy() throws PolicyManagementServiceException, Exception{
		PageResponseDto<SearchAuthPolicy> response = new PageResponseDto<SearchAuthPolicy>();
		 Mockito.when(policyManagementService.searchPolicy(Mockito.any())).thenReturn(response);
		 RequestWrapper<PolicySearchDto> policySearchDto = createPolicySearchhRequest();
		 mockMvc.perform(post("/policy/search").contentType(MediaType.APPLICATION_JSON_VALUE)
	                .content(objectMapper.writeValueAsString(policySearchDto))).andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(roles = {"POLICYMANAGER"})
	public void searchPolicyGroup() throws PolicyManagementServiceException, Exception{
		PageResponseDto<PolicyGroup> response = new PageResponseDto<PolicyGroup>();
		 Mockito.when(policyManagementService.searchPolicyGroup(Mockito.any())).thenReturn(response);
		 RequestWrapper<SearchDto> policyGroupSearchDto = createPolicyGroupSearchhRequest();
		 mockMvc.perform(post("/policyGroup/search").contentType(MediaType.APPLICATION_JSON_VALUE)
	                .content(objectMapper.writeValueAsString(policyGroupSearchDto))).andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(roles = {"POLICYMANAGER"})
	public void policyGroupFilterValuesTest() throws PolicyManagementServiceException, Exception{
		FilterResponseCodeDto response = new FilterResponseCodeDto();
		Mockito.when(policyManagementService.policyGroupFilterValues(Mockito.any())).thenReturn(response);
		RequestWrapper<FilterValueDto> policyGroupFilterValueDto = createPolicyGFilterRequest();
		 mockMvc.perform(post("/policyGroup/filtervalues").contentType(MediaType.APPLICATION_JSON_VALUE)
	                .content(objectMapper.writeValueAsString(policyGroupFilterValueDto))).andExpect(status().isOk());
		
	}
	
	@Test
	@WithMockUser(roles = {"POLICYMANAGER"})
	public void policyFilterValuesTest() throws PolicyManagementServiceException, Exception{
		FilterResponseCodeDto response = new FilterResponseCodeDto();
		Mockito.when(policyManagementService.policyFilterValues(Mockito.any())).thenReturn(response);
		RequestWrapper<PolicyFilterValueDto> policyFilterValueDto = createPolicyFilterrRequest();
		 mockMvc.perform(post("/policy/filtervalues").contentType(MediaType.APPLICATION_JSON_VALUE)
	                .content(objectMapper.writeValueAsString(policyFilterValueDto))).andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(roles = {"POLICYMANAGER"})
	public void policyGroupCreationTest() throws PolicyManagementServiceException, Exception{
		PolicyGroupCreateResponseDto response = new PolicyGroupCreateResponseDto();
		Mockito.when(policyManagementService.createPolicyGroup(Mockito.any())).thenReturn(response);
		RequestWrapper<PolicyGroupCreateRequestDto> request = createPolicyGroupRequest();
		mockMvc.perform(post("/policies/policyGroup").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))).andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(roles = {"POLICYMANAGER"})
	public void policyCreationTest() throws PolicyManagementServiceException, Exception{
		PolicyCreateResponseDto response = new PolicyCreateResponseDto();
		Mockito.when(policyManagementService.createPolicies(Mockito.any())).thenReturn(response);
		RequestWrapper<PolicyCreateRequestDto> request = createPolicyRequest();
		mockMvc.perform(post("/policies").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))).andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(roles = {"POLICYMANAGER"})
	public void updatePolicyGroupTest() throws Exception{
		PolicyGroupCreateResponseDto response = new PolicyGroupCreateResponseDto();
		Mockito.when(policyManagementService.updatePolicyGroup(Mockito.any(),Mockito.any())).thenReturn(response);
		RequestWrapper<PolicyGroupUpdateRequestDto> request = createPolicyGroupUpdateRequest();
		
		mockMvc.perform(put("/policies/policyGroup/12345").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(roles = {"POLICYMANAGER"})
	public void updatePolicyTest() throws Exception{
		PolicyCreateResponseDto response = new PolicyCreateResponseDto();
		Mockito.when(policyManagementService.updatePolicies(Mockito.any(),Mockito.any())).thenReturn(response);
		RequestWrapper<PolicyUpdateRequestDto> request = createPolicyUpdateRequest();
		
		mockMvc.perform(put("/policies/12345").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(roles = {"POLICYMANAGER"})
	public void updatePolicyStatus() throws JsonProcessingException, Exception{
		ResponseWrapper<PolicyStatusUpdateResponseDto> response = new ResponseWrapper<>();
		Mockito.when(policyManagementService.updatePolicyStatus(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(response);
		RequestWrapper<PolicyStatusUpdateRequestDto> request = createPolicyStatusUpateRequest();
		
		mockMvc.perform(MockMvcRequestBuilders.patch("/policies/policyGroupId/12345/policyId/12345").contentType(MediaType.APPLICATION_JSON_VALUE)
    			.content(objectMapper.writeValueAsString(request))).andExpect(MockMvcResultMatchers.status().isOk());

	}
	
	@Test
	@WithMockUser(roles = {"POLICYMANAGER"})
	public void getPoliciesTest() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/policies")).
		andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser(roles = {"POLICYMANAGER"})
	public void getPolicyTest() throws Exception{
		PolicyResponseDto response = new PolicyResponseDto();
		Mockito.when(policyManagementService.findPolicy(Mockito.any())).thenReturn(response);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/policies/policyId/12345")).
		andExpect(MockMvcResultMatchers.status().isOk());		
	}
	
	@Test
	@WithMockUser(roles = {"POLICYMANAGER"})
	public void getPolicyWithApiKeyTest() throws Exception{
		PolicyResponseDto response = new PolicyResponseDto();
		Mockito.when(policyManagementService.findPolicy(Mockito.any())).thenReturn(response);		
		mockMvc.perform(MockMvcRequestBuilders.get("/policies/partnerApiKey/12345")).
		andExpect(MockMvcResultMatchers.status().isOk());		
	}
	
	private RequestWrapper<PolicyStatusUpdateRequestDto> createPolicyStatusUpateRequest() {
		RequestWrapper<PolicyStatusUpdateRequestDto> request = new RequestWrapper<PolicyStatusUpdateRequestDto>();
		request.setRequest(createPolicyStatusUpdateRequest());
        request.setId("mosip.partnermanagement.policies.policy.create");
        request.setVersion("1.0");
        request.setRequesttime(ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime());
        request.setMetadata("{}");
        return request;

	}

	private PolicyStatusUpdateRequestDto createPolicyStatusUpdateRequest() {
		PolicyStatusUpdateRequestDto request = new PolicyStatusUpdateRequestDto();
		request.setStatus("De-Active");
		return request;
	}

	private RequestWrapper<PolicyUpdateRequestDto> createPolicyUpdateRequest() {
		RequestWrapper<PolicyUpdateRequestDto> request = new RequestWrapper<PolicyUpdateRequestDto>();
		PolicyUpdateRequestDto updateRequest = new PolicyUpdateRequestDto();
		updateRequest.setDesc("Update Policy");
		updateRequest.setName("Update Name");
		updateRequest.setPolicyGroupName("Test");
		updateRequest.setVersion("1.0");
		request.setRequest(updateRequest);
        request.setId("mosip.partnermanagement.policies.authPolicies.create");
        request.setVersion("1.0");
        request.setRequesttime(ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime());
        request.setMetadata("{}");
        return request;
	}
	
	private RequestWrapper<PolicyGroupUpdateRequestDto> createPolicyGroupUpdateRequest() {
		RequestWrapper<PolicyGroupUpdateRequestDto> request = new RequestWrapper<PolicyGroupUpdateRequestDto>();
		PolicyGroupUpdateRequestDto updateRequest = new PolicyGroupUpdateRequestDto();
		updateRequest.setDesc("Update Policy");
		updateRequest.setName("Update Name");
		updateRequest.setActive(true);
		request.setRequest(updateRequest);
        request.setId("mosip.partnermanagement.policies.authPolicies.create");
        request.setVersion("1.0");
        request.setRequesttime(ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime());
        request.setMetadata("{}");
        return request;
	}

	
	
//	private PolicyUpdateRequestDto createUpdatePolicyRequest() {
//		PolicyUpdateRequestDto request = new PolicyUpdateRequestDto();
//		request.setPolicies(createAuthPolicyInput());
//		request.setDesc("Policy desc Updated.");
//		request.setName("Updated Policy Name");
//		return request;
//	}

//	private PolicyDto createAuthPolicyInput() {
//		PolicyDto policy = new PolicyDto();
//		policy.setAllowedKycAttributes(getAllowedKycAttributes());
//		policy.setAuthPolicies(getAuthPolicies());
//		return policy;
//	}

	@SuppressWarnings("unused")
	private List<AuthPolicyDto> getAuthPolicies() {
		List<AuthPolicyDto> authPolicies = new ArrayList<AuthPolicyDto>();
		AuthPolicyDto dto = new AuthPolicyDto();		
		dto.setAuthSubType("otp");
		dto.setAuthSubType("none");
		dto.setMandatory(true);
		authPolicies.add(dto);		
		return authPolicies;
	}

	@SuppressWarnings("unused")
	private List<ShareableAttributesDto> getAllowedKycAttributes() {
		List<ShareableAttributesDto> allowedKycList = new ArrayList<ShareableAttributesDto>();
		ShareableAttributesDto dto =  new ShareableAttributesDto();
		dto.setAttributeName("Name");
		dto.setEncrypted(true);
		allowedKycList.add(dto);
		return allowedKycList;
	}

	private RequestWrapper<PolicyGroupCreateRequestDto> createPolicyGroupRequest() {
		RequestWrapper<PolicyGroupCreateRequestDto> request = new RequestWrapper<PolicyGroupCreateRequestDto>();
		request.setRequest(createPolicyGRequest());
        request.setId("mosip.partnermanagement.policies.create");
        request.setVersion("1.0");
        request.setRequesttime(ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime());
        request.setMetadata("{}");
        return request;
	}
	
	private PolicyGroupCreateRequestDto createPolicyGRequest() {
		PolicyGroupCreateRequestDto requestDto = new PolicyGroupCreateRequestDto();
		requestDto.setName("PolicyName");
		requestDto.setDesc("PolicyDesc");
		return requestDto;
	}
	
	private RequestWrapper<PolicyCreateRequestDto> createPolicyRequest() {
		RequestWrapper<PolicyCreateRequestDto> request = new RequestWrapper<PolicyCreateRequestDto>();
		request.setRequest(createPolicyyRequest());
        request.setId("mosip.partnermanagement.policies.create");
        request.setVersion("1.0");
        request.setRequesttime(ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime());
        request.setMetadata("{}");
        return request;
	}
	
	private PolicyCreateRequestDto createPolicyyRequest() {
		JSONObject policies = null;
		PolicyCreateRequestDto requestDto = new PolicyCreateRequestDto();
		requestDto.setPolicyGroupName("mpolicygroup-default-auth");
		requestDto.setPolicyId("mosip.partnermanagement.policies.create");
		requestDto.setPolicyType("AUTH");
		requestDto.setVersion("1.0");
		requestDto.setName("PolicyName");
		requestDto.setDesc("PolicyDesc");
		requestDto.setPolicies(policies);
		return requestDto;
	}
	
	
	private RequestWrapper<PolicySearchDto> createPolicySearchhRequest() {
		RequestWrapper<PolicySearchDto> request = new RequestWrapper<PolicySearchDto>();
		request.setRequest(createPolicySearchRequest());
        request.setId("mosip.partnermanagement.policies.create");
        request.setVersion("1.0");
        request.setRequesttime(ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime());
        request.setMetadata("{}");
        return request;
	}
	private PolicySearchDto createPolicySearchRequest() {
		List<SearchSort> searchDtos1 = new ArrayList<SearchSort>();
		List<SearchFilter> searchDtos = new ArrayList<SearchFilter>();
		PolicySearchDto requestDto = new PolicySearchDto();
		Pagination pagination = new Pagination();
		SearchSort searchSort = new SearchSort();
		SearchDto searchDto = new SearchDto();
		SearchFilter searchFilter = new SearchFilter();
		searchFilter.setColumnName("name");
		searchFilter.setFromValue("");
		searchFilter.setToValue("");
		searchFilter.setType("ALL");
		searchFilter.setValue("m");
    	searchDtos.add(searchFilter);
		searchDto.setFilters(searchDtos);
    	searchSort.setSortField("model");
    	searchSort.setSortType("asc");
    	searchDtos1.add(searchSort);
    	searchDto.setSort(searchDtos1);
    	pagination.setPageFetch(10);
    	pagination.setPageStart(0);
    	requestDto.setPolicyType("AUTH");
    	requestDto.setFilters(searchDtos);
    	requestDto.setSort(searchDtos1);
    	requestDto.setPagination(pagination);
		return requestDto;
	}
	
	private RequestWrapper<SearchDto> createPolicyGroupSearchhRequest() {
		RequestWrapper<SearchDto> request = new RequestWrapper<SearchDto>();
		request.setRequest(createPolicyGroupSearchRequest());
        request.setId("mosip.partnermanagement.policies.create");
        request.setVersion("1.0");
        request.setRequesttime(ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime());
        request.setMetadata("{}");
        return request;
	}
	private SearchDto createPolicyGroupSearchRequest() {
		List<SearchSort> searchDtos1 = new ArrayList<SearchSort>();
		List<SearchFilter> searchDtos = new ArrayList<SearchFilter>();
		SearchDto requestDto = new SearchDto();
		Pagination pagination = new Pagination();
		SearchSort searchSort = new SearchSort();
		SearchFilter searchFilter = new SearchFilter();
		searchFilter.setColumnName("name");
		searchFilter.setFromValue("");
		searchFilter.setToValue("");
		searchFilter.setType("ALL");
		searchFilter.setValue("m");
		searchSort.setSortField("model");
    	searchSort.setSortType("asc");
    	searchDtos.add(searchFilter);
    	searchDtos1.add(searchSort);
    	pagination.setPageFetch(10);
    	pagination.setPageStart(0);
    	requestDto.setFilters(searchDtos);
    	requestDto.setSort(searchDtos1);
    	requestDto.setPagination(pagination);
		return requestDto;
	}
	
	private RequestWrapper<FilterValueDto> createPolicyGFilterRequest() {
		RequestWrapper<FilterValueDto> request = new RequestWrapper<FilterValueDto>();
		request.setRequest(createPolicyGroupFilterRequest());
        request.setId("mosip.partnermanagement.policies.create");
        request.setVersion("1.0");
        request.setRequesttime(ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime());
        request.setMetadata("{}");
        return request;
	}
	private FilterValueDto createPolicyGroupFilterRequest() {
		List<FilterDto> filterDtos = new ArrayList<FilterDto>();
		List<SearchFilter> searchDtos = new ArrayList<SearchFilter>();
		FilterDto filterDto = new FilterDto();
		SearchFilter searchFilter = new SearchFilter();
		FilterValueDto filterValueDto = new FilterValueDto();
		PolicyFilterValueDto policyFilterValueDto = new PolicyFilterValueDto();
		policyFilterValueDto.setPolicyType("");
    	policyFilterValueDto.setFilters(filterDtos);
    	policyFilterValueDto.setOptionalFilters(searchDtos);
		filterDto.setColumnName("name");
		filterDto.setText("");
		filterDto.setType("ALL");
		searchFilter.setColumnName("name");
		searchFilter.setFromValue("");
		searchFilter.setToValue("");
		searchFilter.setType("ALL");
		searchFilter.setValue("m");
    	filterDtos.add(filterDto);
    	searchDtos.add(searchFilter);
    	filterValueDto.setFilters(filterDtos);
    	filterValueDto.setOptionalFilters(searchDtos);
    	return filterValueDto;
	}
	
	private RequestWrapper<PolicyFilterValueDto> createPolicyFilterrRequest() {
		RequestWrapper<PolicyFilterValueDto> request = new RequestWrapper<PolicyFilterValueDto>();
		request.setRequest(createPolicyFilterRequest());
        request.setId("mosip.partnermanagement.policies.create");
        request.setVersion("1.0");
        request.setRequesttime(ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime());
        request.setMetadata("{}");
        return request;
	}
	private PolicyFilterValueDto createPolicyFilterRequest() {
		List<FilterDto> filterDtos = new ArrayList<FilterDto>();
		List<SearchFilter> searchDtos = new ArrayList<SearchFilter>();
		FilterDto filterDto = new FilterDto();
		SearchFilter searchFilter = new SearchFilter();
		PolicyFilterValueDto policyFilterValueDto = new PolicyFilterValueDto();
		filterDto.setColumnName("name");
		filterDto.setText("");
		filterDto.setType("ALL");
		searchFilter.setColumnName("name");
		searchFilter.setFromValue("");
		searchFilter.setToValue("");
		searchFilter.setType("ALL");
		searchFilter.setValue("m");
    	filterDtos.add(filterDto);
    	searchDtos.add(searchFilter);
    	policyFilterValueDto.setPolicyType("AUTH");
    	policyFilterValueDto.setFilters(filterDtos);
    	policyFilterValueDto.setOptionalFilters(searchDtos);
    	return policyFilterValueDto;
	}

}
