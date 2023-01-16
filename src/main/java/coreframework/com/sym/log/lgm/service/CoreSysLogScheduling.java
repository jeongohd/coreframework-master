package coreframework.com.sym.log.lgm.service;

import coreframework.com.sym.log.lgm.mapper.CoreSysLogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Class Name : CoreSysLogScheduling.java
 * @Description : 시스템 로그 요약을 위한 스케쥴링 클래스
 * @Modification Information
 *
 *    수정일       수정자         수정내용
 *    -------        -------     -------------------
 *    2009. 3. 11.     이삼섭   최초생성
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 3. 11.
 * @version
 * @see
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("CoreSysLogScheduling")
public class CoreSysLogScheduling extends EgovAbstractMapper {
	private static final Logger LOGGER = LoggerFactory.getLogger(CoreSysLogScheduling.class);


	@Resource(name="CoreSysLogMapper")
	private CoreSysLogMapper coreSysLogMapper;

	/**
	 * 시스템 로그정보를 요약한다.
	 * 전날의 로그를 요약하여 입력하고, 6개월전의 로그를 삭제한다.
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	public void sysLogSummary() throws Exception {
		coreSysLogMapper.logInsertSysLogSummary();
		coreSysLogMapper.logDeleteSysLogSummary();

	}

}
