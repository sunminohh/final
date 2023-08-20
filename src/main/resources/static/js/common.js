const updateQueryString = (key, value) => {
    const url = new URL(window.location.href);
    url.searchParams.set(key, value);
    window.history.pushState(null, null, url.toString());
}

const getQueryString = (key) => {
    const url = new URL(window.location.href);
    return url.searchParams.get(key);
}

const toggleLoading = (isVisible = true) => {
    const display = isVisible ? 'flex' : 'none';
    document.querySelector(".loading-wrap").style.display = display;
}