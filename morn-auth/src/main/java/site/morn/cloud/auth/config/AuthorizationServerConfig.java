/*
 *
 *  *  Copyright (c) 2019-2020, 冷冷 (wangiegie@gmail.com).
 *  *  <p>
 *  *  Licensed under the GNU Lesser General Public License 3.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *  <p>
 *  * https://www.gnu.org/licenses/lgpl.html
 *  *  <p>
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package site.morn.cloud.auth.config;

import site.morn.cloud.common.core.constant.CacheConstants;
import site.morn.cloud.common.core.constant.SecurityConstants;
import site.morn.cloud.common.security.component.PigWebResponseExceptionTranslator;
import site.morn.cloud.common.security.service.PigClientDetailsService;
import site.morn.cloud.common.security.service.PigUser;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author lengleng
 * @date 2019/2/1 认证服务器配置
 */
@Configuration
@RequiredArgsConstructor
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

  private final DataSource dataSource;
  private final UserDetailsService userDetailsService;
  private final AuthenticationManager authenticationManager;
  private final RedisConnectionFactory redisConnectionFactory;

  @Override
  @SneakyThrows
  public void configure(ClientDetailsServiceConfigurer clients) {
    PigClientDetailsService clientDetailsService = new PigClientDetailsService(dataSource);
    clientDetailsService.setSelectClientDetailsSql(SecurityConstants.DEFAULT_SELECT_STATEMENT);
    clientDetailsService.setFindClientDetailsSql(SecurityConstants.DEFAULT_FIND_STATEMENT);
    clients.withClientDetails(clientDetailsService);
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
    oauthServer
        .allowFormAuthenticationForClients()
        .checkTokenAccess("permitAll()");
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
    endpoints
        .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
        .tokenStore(tokenStore())
        .tokenEnhancer(tokenEnhancer())
        .userDetailsService(userDetailsService)
        .authenticationManager(authenticationManager)
        .reuseRefreshTokens(false)
        .pathMapping("/oauth/confirm_access", "/token/confirm_access")
        .exceptionTranslator(new PigWebResponseExceptionTranslator());
  }

  @Bean
  public TokenStore tokenStore() {
    RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
    tokenStore.setPrefix(CacheConstants.PROJECT_OAUTH_ACCESS);
    return tokenStore;
  }

  @Bean
  public TokenEnhancer tokenEnhancer() {
    return (accessToken, authentication) -> {
      final Map<String, Object> additionalInfo = new HashMap<>(4);
      PigUser pigUser = (PigUser) authentication.getUserAuthentication().getPrincipal();
      additionalInfo.put(SecurityConstants.DETAILS_LICENSE, SecurityConstants.PROJECT_LICENSE);
      additionalInfo.put(SecurityConstants.DETAILS_USER_ID, pigUser.getId());
      additionalInfo.put(SecurityConstants.DETAILS_USERNAME, pigUser.getUsername());
      additionalInfo.put(SecurityConstants.DETAILS_DEPT_ID, pigUser.getDeptId());
      ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
      return accessToken;
    };
  }
}
