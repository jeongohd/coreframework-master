package coreframework.com.sts.ust.service;

import coreframework.com.sts.com.StatsVO;
import coreframework.com.sts.ust.mapper.CoreUserStatsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 사용자 통계 검색 비즈니스 구현 클래스
 * @author 공통서비스 개발팀 박지욱
 * @since 2009.03.12
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일     수정자          수정내용
 *  -------    --------    ---------------------------
 *  2009.03.19  박지욱          최초 생성
 *  2011.06.30  이기하          패키지 분리(sts -> sts.ust)
 *
 *  </pre>
 */
@Slf4j
@RequiredArgsConstructor
@Service("CoreUserStatsService")
public class CoreUserStatsService extends EgovAbstractMapper  {
	private static final Logger LOGGER = LoggerFactory.getLogger(CoreUserStatsService.class);
//    @Resource(name="userStatsDAO")
//    private UserStatsDAO userStatsDAO;

	@Resource(name="CoreUserStatsMapper")
	private CoreUserStatsMapper coreUserStatsMapper;
    /**
	 * 사용자 통계를 조회한다
	 * @param vo StatsVO
	 * @return List
	 * @exception Exception
	 */

	public List<?> selectUserStats(StatsVO vo) throws Exception {
        return coreUserStatsMapper.selectUserStats(vo);
	}

    /**
	 * 사용자 통계를 위한 집계를 하루단위로 작업하는 배치 프로그램
	 * @exception Exception
	 */

	public void summaryUserStats() throws Exception {
		coreUserStatsMapper.summaryUserStats();
	}
}
