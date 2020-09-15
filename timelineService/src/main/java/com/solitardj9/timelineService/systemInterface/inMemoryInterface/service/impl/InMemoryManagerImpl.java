package com.solitardj9.timelineService.systemInterface.inMemoryInterface.service.impl;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.LockConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.solitardj9.timelineService.systemInterface.inMemoryInterface.model.InMemoryInstance;
import com.solitardj9.timelineService.systemInterface.inMemoryInterface.model.InMemoryParamEnum.InMemoryMode;
import com.solitardj9.timelineService.systemInterface.inMemoryInterface.service.InMemoryEventListener;
import com.solitardj9.timelineService.systemInterface.inMemoryInterface.service.InMemoryManager;
import com.solitardj9.timelineService.systemInterface.inMemoryInterface.service.impl.exception.ExceptionHazelcastDataStructureCreationFailure;
import com.solitardj9.timelineService.systemInterface.inMemoryInterface.service.impl.exception.ExceptionHazelcastDataStructureNotFoundFailure;
import com.solitardj9.timelineService.systemInterface.inMemoryInterface.service.impl.exception.ExceptionHazelcastInstanceNotFoundFailure;
import com.solitardj9.timelineService.systemInterface.inMemoryInterface.service.impl.exception.ExceptionHazelcastNetworkFailure;

@Service("inMemoryManager")
public class InMemoryManagerImpl implements InMemoryManager {
	//
	private static final Logger logger = LoggerFactory.getLogger(InMemoryManagerImpl.class);
	
	@Value("${systemInterface.inMemoryInterface.inMemoryManager.operation.mode}")
	private String mode;
	
	@Value("${systemInterface.inMemoryInterface.inMemoryManager.operation.groupName}")
	private String groupName;
	
	@Value("${systemInterface.inMemoryInterface.inMemoryManager.network.aws.access-key}")
	private String awsAccessKey;
	
	@Value("${systemInterface.inMemoryInterface.inMemoryManager.network.aws.secret-key}")
	private String awsSecretKey;
	
	@Value("${systemInterface.inMemoryInterface.inMemoryManager.network.aws.region}")
	private String awsRegion;

	@Value("${systemInterface.inMemoryInterface.inMemoryManager.network.aws.hz-port}")
	private String awsHzPort;
	
	@Value("${systemInterface.inMemoryInterface.inMemoryManager.network.aws.usePublicIp}")
	private String awsUsePublicIp;
	
	@Value("${systemInterface.inMemoryInterface.inMemoryManager.network.local.port.autoIncrement}")
	private Boolean localPortAutoIncrement;
	
	@Value("${systemInterface.inMemoryInterface.inMemoryManager.network.local.port.num}")
	private Integer localPortNum;
	
	@Value("${systemInterface.inMemoryInterface.inMemoryManager.network.local.port.count}")
	private Integer localPortCount;
	
	@Value("${systemInterface.inMemoryInterface.inMemoryManager.network.local.tcpip.members}")
	private String localTcpIpMembers;
	
	@Value("${systemInterface.inMemoryInterface.inMemoryManager.network.local.multicast.group}")
	private String localMulticastGroup;
	
	@Value("${systemInterface.inMemoryInterface.inMemoryManager.network.local.multicast.port}")
	private Integer localMulticastPort;
	
	private Config cfg;
	private String cfgName = "timeline-service-cfg";
	
	private HazelcastInstance instance;
	
	private ObjectMapper om = new ObjectMapper();
	
	@PostConstruct
	public void initialize() {
		//
		try {
			createHazelcastInstance();
		} catch (ExceptionHazelcastInstanceNotFoundFailure e) {
			logger.error("[InMemoryManager].initialize : fail to initialize InMemoryManager");
		}
		logger.info("[InMemoryManager].initialize : success to initialize InMemoryManager");
	}
	
	private void createHazelcastInstance() throws ExceptionHazelcastInstanceNotFoundFailure {
		//
		try {
			cfg = new Config();
			cfg.setInstanceName(cfgName);
			cfg.getGroupConfig().setName(groupName);
			
			setupNetwork();
			setupCluster();
			
			instance = Hazelcast.newHazelcastInstance(cfg);
		}
		catch (Exception e) {
			throw new ExceptionHazelcastInstanceNotFoundFailure();
		}
	}
	
	/**
	 *  • setPortAutoIncrement : Sets if a Hazelcast member is allowed to find a free port by incrementing the port number when it encounters an occupied port
	 *  • setPortCount : The maximum number of ports allowed to use.
	 */
	private void setupNetwork() {
		//
		if (mode.equals(InMemoryMode.LOCAL.getMode())) {
			// TCP-IP or Multi-Cast
			NetworkConfig network = cfg.getNetworkConfig();
			network.setPortAutoIncrement(localPortAutoIncrement).setPort(localPortNum).setPortCount(localPortCount);
		}
		else if (mode.equals(InMemoryMode.AWS.getMode())) {
			//
			NetworkConfig network = cfg.getNetworkConfig();
			network.getInterfaces().setEnabled(false);
		}
		else {
			//
		}
	}
	
	/**
	 * The multicast element includes parameters to fine tune the multicast join mechanism.
	 *  • enabled: Specifies whether the multicast discovery is enabled or not, true or false.
	 *  • multicast-group: The multicast group IP address. Specify it when you want to create clusters within the same network. 
	 *  				   Values can be between 224.0.0.0 and 239.255.255.255. Default value is 224.2.2.3.
	 *  • multicast-port: The multicast socket port that the Hazelcast member listens to and sends discovery messages through. Default value is 54327.
	 *  • multicast-time-to-live: Time-to-live value for multicast packets sent out to control the scope of multicasts. See more information here.
	 *  • multicast-timeout-seconds: Only when the members are starting up, this timeout (in seconds) specifies the period during which a member waits for a multicast response from another member. 
	 *  							 For example, if you set it as 60 seconds, each member will wait for 60 seconds until a leader member is selected. Its default value is 2 seconds.
	 *  • trusted-interfaces: Includes IP addresses of trusted members. When a member wants to join to the cluster, its join request will be rejected if it is not a trusted member. 
	 *  					  You can give an IP addresses range using the wildcard (*) on the last digit of IP address, e.g., 192.168.1.* or 192.168.1.100-110. 
	 *  
	 *  • Set the enabled attribute of the multicast element to "true".
	 *  • Set multicast-group, multicast-port, multicast-time-to-live, etc. to your multicast values.
	 *  • Set the enabled attribute of both tcp-ip and aws elements to "false".
	 *  
	 *  The following is an example declarative configuration.
	 *  <hazelcast>
	 *     ...
	 *     <network>
	 *     ...
	 *	        <join>
	 *	            <multicast enabled="true">
	 *	                <multicast-group>224.2.2.3</multicast-group>
	 *	                <multicast-port>54327</multicast-port>
	 *	                <multicast-time-to-live>32</multicast-time-to-live>
	 *	                <multicast-timeout-seconds>2</multicast-timeout-seconds>
	 *	                <trusted-interfaces>
	 *	                   <interface>192.168.1.102</interface>
	 *	                </trusted-interfaces>   
	 *	            </multicast>
	 *	            <tcp-ip enabled="false">
	 *	            </tcp-ip>
	 *	            <aws enabled="false">
	 *	            </aws>
	 *	        </join>
	 *	  <network>
	 */
	@SuppressWarnings("unchecked")
	private void setupCluster() throws ExceptionHazelcastNetworkFailure {
		//
		try {
			if (mode.equals(InMemoryMode.LOCAL.getMode())) {
				//
				if (!localTcpIpMembers.contentEquals("{}")) {		// TCP-IP
					//
					Map<String, String> members = om.readValue(localTcpIpMembers, Map.class);
					
					NetworkConfig network = cfg.getNetworkConfig();
					network.setReuseAddress(true);
					JoinConfig join = network.getJoin();
					
					TcpIpConfig tcpipConfig = join.getTcpIpConfig();
					tcpipConfig.setEnabled(true);
					for (Entry<String, String> entry : members.entrySet()) {
						tcpipConfig.addMember(entry.getValue());
					}
					join.getMulticastConfig().setEnabled(false);
					join.getAwsConfig().setEnabled(false);
				}
				else {	// Multi-Cast
					//
					NetworkConfig network = cfg.getNetworkConfig();
					network.setReuseAddress(true);
					JoinConfig join = network.getJoin();
					
					join.getTcpIpConfig().setEnabled(false);
					join.getMulticastConfig().setEnabled(true).setMulticastGroup(localMulticastGroup).setMulticastPort(localMulticastPort);
					join.getAwsConfig().setEnabled(false);
				}
			}
			else if (mode.equals(InMemoryMode.AWS.getMode())) {
				//
				NetworkConfig network = cfg.getNetworkConfig();
				JoinConfig join = network.getJoin();
				
				join.getTcpIpConfig().setEnabled(false);
				join.getMulticastConfig().setEnabled(false);
				join.getAwsConfig().setEnabled(true).setProperty("access-key", awsAccessKey).setProperty("secret-key", awsSecretKey).setProperty("region", awsRegion).setProperty("hz-port", awsHzPort).setProperty("use-public-ip", awsUsePublicIp);
			}
			else {
				//
			}
		} catch (Exception e) {
			logger.error("[InMemoryManager].setupCluster : fail to cluster InMemoryManager = " + e.getStackTrace());
			throw new ExceptionHazelcastNetworkFailure();
		}
	}
	
	@Override
	public IMap<Object, Object> addMap(InMemoryInstance inMemoryInstance)  throws ExceptionHazelcastDataStructureCreationFailure, ExceptionHazelcastDataStructureNotFoundFailure {
		//
		MapConfig mapConfig = new MapConfig();
		
		String mapName = inMemoryInstance.getName();
		if (mapName == null || mapName.isEmpty()) {
			throw new ExceptionHazelcastDataStructureCreationFailure();
		}
		mapConfig.setName(mapName);
		
		Integer backupCount = inMemoryInstance.getBackupCount();
		if (backupCount != null) {
			mapConfig.setBackupCount(backupCount);
		}
		
		Boolean readBackupData = inMemoryInstance.getReadBackupData();
		if (backupCount != null) {
			mapConfig.setReadBackupData(readBackupData);
		}
		
		instance.getConfig().addMapConfig(mapConfig);
		
		String lockName = inMemoryInstance.getLockName();
		if (lockName != null && !lockName.isEmpty()) {
			LockConfig lockConfig = new LockConfig();
			lockConfig.setName(lockName).setQuorumName("quorum-name");
			instance.getConfig().addLockConfig(lockConfig);
        }
		
		InMemoryEventListener eventListener = inMemoryInstance.getEventListener();
		if (eventListener != null) {
			try {
				getMap(mapName).addEntryListener(eventListener, true);
			} catch (ExceptionHazelcastDataStructureNotFoundFailure e) {
				logger.error("[InMemoryManager].addMap : error = " + e.getStackTrace());
			}
		}
		
		try {
			return getMap(mapName);
		} catch (ExceptionHazelcastDataStructureNotFoundFailure e) {
			logger.error("[InMemoryManager].addMap : error = " + e.getStackTrace());
			throw new ExceptionHazelcastDataStructureNotFoundFailure();
		}
	}
	
	@Override
	public IMap<Object, Object> getMap(String mapName) throws ExceptionHazelcastDataStructureNotFoundFailure {
		//
		if (mapName == null || mapName.isEmpty()) {
			throw new ExceptionHazelcastDataStructureNotFoundFailure();
		}
		
		return instance.getMap(mapName);
	}
}