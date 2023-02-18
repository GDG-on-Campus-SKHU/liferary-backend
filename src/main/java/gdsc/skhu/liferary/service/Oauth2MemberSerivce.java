package gdsc.skhu.liferary.service;

import gdsc.skhu.liferary.domain.DTO.OAuth2Attribute;
import gdsc.skhu.liferary.domain.Member;
import gdsc.skhu.liferary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class Oauth2MemberSerivce implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        /* OAuth2 서비스 ID */
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        /* OAuth2 로그인 시 키가 되는 필드 */
        String usernameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        /* OAuth2UserService */
        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(registrationId, usernameAttributeName, oAuth2User.getAttributes());
        Member member = this.save(oAuth2Attribute);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRoles().get(0))),
                oAuth2Attribute.getAttributes(),
                oAuth2Attribute.getAttributeKey()
        );
    }

    private Member save(OAuth2Attribute attributes) {
        Member member = memberRepository.findByEmail(attributes.getEmail()).orElse(attributes.toEntity());
        return memberRepository.save(member);
    }
}
