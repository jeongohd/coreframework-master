package coreframework.com.sts.ust.web;

import coreframework.com.cmm.ComDefaultCodeVO;
import coreframework.com.cmm.annotation.IncludedInfo;
import coreframework.com.cmm.service.CoreCmmUseService;
import coreframework.com.sts.com.StatsVO;
import coreframework.com.sts.ust.service.CoreUserStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * 사용자 통계 검색 컨트롤러 클래스
 * @author 공통서비스 개발팀 박지욱
 * @since 2009.03.19
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일     수정자          수정내용
 *  -------    --------    ---------------------------
 *  2009.03.19  박지욱          최초 생성
 *  2011.06.30  이기하          패키지 분리(sts -> sts.sst)
 *  2011.8.26	정진오			IncludedInfo annotation 추가
 *
 *  </pre>
 */

@Slf4j
@RequiredArgsConstructor
@Controller
public class CoreUserStatsController {

	/** CoreUserStatsMapper */
	@Resource(name = "CoreUserStatsService")
    private CoreUserStatsService coreUserStatsService;

	/** CoreCmmUseService */
	@Resource(name="CoreCmmUseService")
	private CoreCmmUseService cmmUseService;

    /** log */
	private static final Logger LOGGER = LoggerFactory.getLogger(CoreUserStatsController.class);

    /**
	 * 사용자 통계를 조회한다
	 * @param statsVO StatsVO
	 * @return String
	 * @exception Exception
	 */
    @IncludedInfo(name="사용자통계", listUrl="/sts/ust/coreUserStatsList.cm", order = 130 ,gid = 30)
    @RequestMapping(value="/sts/ust/coreUserStatsList.cm")
	public String coreUserStatsList(@ModelAttribute("statsVO") StatsVO statsVO,
			ModelMap model) throws Exception {

    	// 세부통계구분 공통코드 목록 조회(회원유형,상태,성별에 대한 세부통계구분코드)
    	ComDefaultCodeVO vo = new ComDefaultCodeVO();

    	vo.setCodeId("COM012");
		List<?> code012 = cmmUseService.selectCmmCodeDetail(vo);
		vo.setCodeId("COM013");
		List<?> code013 = cmmUseService.selectCmmCodeDetail(vo);
		vo.setCodeId("COM014");
		List<?> code014 = cmmUseService.selectCmmCodeDetail(vo);

		model.addAttribute("COM012", code012);
		model.addAttribute("COM013", code013);
		model.addAttribute("COM014", code014);

		if (statsVO.getFromDate() != null && !"".equals(statsVO.getFromDate())) {

			List<?> userStats = coreUserStatsService.selectUserStats(statsVO);
			LOGGER.debug("++++++++++++++++++++++ userStats.size() : {}", userStats.size());
			// 그래프에 표시될 이미지 길이를 결정한다.
			float iMaxUnit = 50.0f;
			for (int i = 0; i < userStats.size(); i++) {
				StatsVO sVo = (StatsVO)userStats.get(i);
				int iCnt = sVo.getStatsCo();
				if (iCnt > 10 && iCnt <= 100) {
					if (iMaxUnit > 5.0f) {
						iMaxUnit = 5.0f;
					}
				} else if (iCnt > 100 && iCnt <= 1000) {
					if (iMaxUnit > 0.5f) {
						iMaxUnit = 0.5f;
					}
				} else if (iCnt > 1000) {
					if (iMaxUnit > 0.05f) {
						iMaxUnit = 0.05f;
					}
				}
			}
			statsVO.setMaxUnit(iMaxUnit);

			model.addAttribute("userStats", userStats);
			model.addAttribute("statsInfo", statsVO);
		}
        return "coreframework/com/sts/ust/CoreUserStats";
	}
}