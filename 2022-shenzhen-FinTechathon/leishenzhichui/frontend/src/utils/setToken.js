const tokenKey = 'token';

export function setToken(token) {
    return sessionStorage.setItem(tokenKey, token);
}

export function getToken() {
    return sessionStorage.getItem(tokenKey);
}

export function removeToken() {
    return sessionStorage.removeItem(tokenKey);
}
