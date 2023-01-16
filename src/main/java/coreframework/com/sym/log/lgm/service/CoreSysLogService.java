package coreframework.com.sym.log.lgm.service;

import coreframework.com.sym.log.lgm.mapper.CoreSysLogMapper;
import coreframework.com.sym.log.lgm.vo.SysLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Class Name : CoreSysLogService.java
 * @Description : 로그관리(시스템)를 위한 서비스 구현 클래스
 * @Modification Information
 *
 *    수정일       수정자         수정내용
 *    -------        -------     -------------------
 *    2009. 3. 11.     이삼섭
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 3. 11.
 * @version
 * @see
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("CoreSysLogService")
public class CoreSysLogService extends EgovAbstractMapper {
	private static final Logger LOGGER = LoggerFactory.getLogger(CoreSysLogService.class);
	@Resource(name="CoreSysLogMapper")
	private CoreSysLogMapper coreSysLogMapper;

    /** ID Generation */
	@Resource(name="egovSysLogIdGnrService")
	private EgovIdGnrService egovSysLogIdGnrService;

	/**
	 * 시스템 로그정보를 생성한다.
	 *
	 * @param SysLog
	 */

	public void logInsertSysLog(SysLog sysLog) throws Exception {
		String requstId = egovSysLogIdGnrService.getNextStringId();
		sysLog.setRequstId(requstId);

		coreSysLogMapper.logInsertSysLog(sysLog);
		
	}

	/**
	 * 시스템 로그정보를 요약한다.
	 *
	 * @param
	 */

	public void logInsertSysLogSummary() throws Exception {
		coreSysLogMapper.logInsertSysLogSummary();
		
	}

	/**
	 * 시스템 로그정보 목록을 조회한다.
	 *
	 * @param SysLog
	 */

	public Map<?, ?> selectSysLogInf(SysLog sysLog) throws Exception {

		List<?> _result = coreSysLogMapper.selectSysLogInf(sysLog);
		int _cnt = coreSysLogMapper.selectSysLogInfCnt(sysLog);

		Map<String, Object> _map = new HashMap<String, Object>();
		_map.put("resultList", _result);
		_map.put("resultCnt", Integer.toString(_cnt));

		return _map;
	}

	/**
	 * 시스템 로그 상세정보를 조회한다.
	 *
	 * @param sysLog
	 * @return sysLog
	 * @throws Exception
	 */

	public SysLog selectSysLog(SysLog sysLog) throws Exception {
		return coreSysLogMapper.selectSysLog(sysLog);
	}

}
