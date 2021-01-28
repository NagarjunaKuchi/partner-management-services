package io.mosip.pmp.partner.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import io.mosip.pmp.common.helper.WebSubPublisher;


/**
 * @author sanjeev.shrivastava
 *
 */
@Import(value = {WebSubPublisher.class})
@SpringBootApplication(scanBasePackages = {"io.mosip.pmp.keycloak.*","io.mosip.pmp.partner.*","io.mosip.pmp.authdevice.*","io.mosip.pmp.regdevice.*","io.mosip.pmp.common.*"})
public class PartnerserviceApplicationTest {
	
	/**
	 * Function to run the Master-Data-Service application
	 * 
	 * @param args The arguments to pass will executing the main function
	 */
	public static void main(String[] args) {
		SpringApplication.run(PartnerserviceApplicationTest.class, args);
	}
}
