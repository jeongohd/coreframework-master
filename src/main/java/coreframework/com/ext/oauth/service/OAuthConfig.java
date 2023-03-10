package coreframework.com.ext.oauth.service;

public interface OAuthConfig {

	static final String GOOGLE_PROFILE_URL = "https://www.googleapis.com/plus/v1/people/me";
	static final String NAVER_PROFILE_URL = "https://openapi.naver.com/v1/nid/me";
	static final String KAKAO_PROFILE_URL = "https://kapi.kakao.com/v2/user/me";

	static final String NAVER_ACCESS_TOKEN = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code";
	static final String NAVER_AUTH = "https://nid.naver.com/oauth2.0/authorize";

	static final String KAKAO_ACCESS_TOKEN = "https://kauth.kakao.com/oauth/token?client_id=";
	static final String KAKAO_AUTH = "https://kauth.kakao.com/oauth/authorize";

	static final String GOOGLE_SERVICE_NAME = "google";
	static final String NAVER_SERVICE_NAME = "naver";
	static final String KAKAO_SERVICE_NAME = "kakao";
}
