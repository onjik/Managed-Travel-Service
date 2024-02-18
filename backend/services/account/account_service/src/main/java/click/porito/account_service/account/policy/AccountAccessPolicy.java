package click.porito.account_service.account.policy;

import click.porito.managed_travel.domain.api.response.AccountSummaryResponse;
import click.porito.managed_travel.domain.domain.Account;
import click.porito.account_service.account.operation.AccountOperation;
import click.porito.account_service.security.domain.Action;
import click.porito.account_service.security.domain.Authority;
import click.porito.account_service.security.domain.PermissionAuthority;
import click.porito.account_service.security.domain.Scope;
import click.porito.common.exception.Domain;
import click.porito.common.exception.common.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AccountAccessPolicy implements PermissionEvaluator {
    private final RoleHierarchy roleHierarchy;
    private final AccountOperation accountOperation;
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        String userId = authentication.getName();
        List<Authority> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(Authority::parseAuthority)
                .filter(Objects::nonNull)
                .toList();
        Action action = Action.valueOf(permission.toString().toUpperCase());
        if (targetDomainObject instanceof Account account) {
            return hasPermissionToAccount(userId, authorities, account.getUserId(), action);
        } else if (targetDomainObject instanceof AccountSummaryResponse summary) {
            return hasPermissionToAccountSummary(userId, authorities, summary.userId(), action);
        }
        return false;
    }

    private boolean hasPermissionToAccount(String userId, List<Authority> authorities, String targetId, Action permission) {
        // permission.equals(Action.READ)
        Set<Scope> scopes = authorities.stream()
                .filter(authority -> authority instanceof PermissionAuthority)
                .map(authority -> (PermissionAuthority) authority)
                .filter(authority -> Domain.ACCOUNT.equals(authority.domain()))
                .filter(authority -> Action.READ.equals(authority.action()))
                .map(PermissionAuthority::scope)
                .collect(Collectors.toSet());

        if (scopes.contains(Scope.ALL)) {
            return true;
        }
        if (scopes.contains(Scope.OWNED)) {
            return targetId.equals(userId);
        }
        return false;
    }


    private boolean hasPermissionToAccountSummary(String userId, List<Authority> authorities, String targetId, Action permission) {
        if (!permission.equals(Action.READ)){
            return false;
        }

        Set<Scope> scopes = authorities.stream()
                .filter(authority -> authority instanceof PermissionAuthority)
                .map(authority -> (PermissionAuthority) authority)
                .filter(authority -> Domain.ACCOUNT.equals(authority.domain()))
                .filter(authority -> Action.READ.equals(authority.action()))
                .map(PermissionAuthority::scope)
                .collect(Collectors.toSet());

        if (scopes.contains(Scope.ALL)) {
            return true;
        }
        if (scopes.contains(Scope.OWNED)) {
            return userId.equals(targetId);
        }
        return scopes.contains(Scope.SUMMARY);
    }


    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        String userId = authentication.getName();
        List<Authority> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(Authority::parseAuthority)
                .filter(Objects::nonNull)
                .toList();
        Action action = Action.valueOf(permission.toString().toUpperCase());
        Account account = accountOperation.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(Domain.ACCOUNT));
        if (AccountSummaryResponse.class.getSimpleName().equals(targetType)) {
            return hasPermissionToAccountSummary(userId, authorities, account.getUserId(), action);
        } else if (Account.class.getSimpleName().equals(targetType)) {
            return hasPermissionToAccount(userId, authorities, account.getUserId(), action);
        }
        return false;
    }
}
