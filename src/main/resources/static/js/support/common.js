function renderPagination(pagination) {
    let paginationHtml = "";

    if (!pagination.first) {
        paginationHtml += createPaginationItem(1, false, 'control first');
    }
    if (pagination.currentBlock > 1) {
        const prePage = (pagination.currentBlock - 1) * 10;
        paginationHtml += createPaginationItem(prePage, pagination.page === prePage, 'control prev');
    }

    for (let i = pagination.beginPage; i <= pagination.endPage; i++) {
        paginationHtml += createPaginationItem(i, pagination.page === i);
    }

    if (pagination.currentBlock < pagination.totalBlocks) {
        const nextPage = pagination.currentBlock * 10 + 1;
        paginationHtml += createPaginationItem(nextPage, pagination.page === nextPage, 'control next');
    }

    if (!pagination.last) {
        paginationHtml += createPaginationItem(pagination.totalPages, pagination.page === pagination.totalPages, 'control last');
    }
    return paginationHtml;
}

function createPaginationItem(page, isActive = false, className = '') {
    return `
        <li class="page-item">
            <a href="list?page=${page}" 
                class="page-link page-number-link ${className} ${isActive ? 'active' : ''}" 
                data-page="${page}">${page}</a>
        </li>
    `;
}