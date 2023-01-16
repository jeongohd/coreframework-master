package coreframework.com.sym.log.lgm.mapper;

import coreframework.com.sym.log.lgm.vo.SysLog;
import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Class Name : CoreSysLogMapper.java
 * @Description : 로그관리(시스템)를 위한 서비스 인터페이스
 * @Modification Information
 *
 *    수정일       수정자         수정내용
 *    -------      -------     -------------------
 *    2009. 3. 11.  이삼섭      최초생성
 *    2011. 7. 01.  이기하      패키지 분리(sym.log -> sym.log.lgm)
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 3. 11.
 * @version
 * @see
 *
 */

@Mapper("CoreSysLogMapper")
@Repository
public interface CoreSysLogMapper {
	
	/**
	 * 시스템 로그정보를 생성한다.
	 *
	 * @param SysLog
	 */
	public void logInsertSysLog(SysLog sysLog) throws Exception;

	/**
	 * 시스템 로그정보를 요약한다.
	 *
	 * @param
	 */
	public void logInsertSysLogSummary() throws Exception;
	
	/**
	 * 시스템 로그정보 목록을 조회한다.
	 *
	 * @param SysLog
	 */
	public List<?> selectSysLogInf(SysLog sysLog) throws Exception;

	/**
	 * 시스템로그 상세정보를 조회한다.
	 *
	 * @param sysLog
	 * @return sysLog
	 * @throws Exception
	 */
	public SysLog selectSysLog(SysLog sysLog) throws Exception;

	public int selectSysLogInfCnt(SysLog sysLog) throws Exception;

	public void logDeleteSysLogSummary() throws Exception;
}
