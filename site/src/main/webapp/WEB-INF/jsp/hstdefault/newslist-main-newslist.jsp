<%@ include file="/WEB-INF/jsp/include/imports.jsp" %>
<%--
  Copyright 2014 Hippo B.V. (http://www.onehippo.com)

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  --%>

<%--@elvariable id="document" type="org.example.beans.NewsDocument"--%>
<%--@elvariable id="pageable" type="org.onehippo.cms7.essentials.components.paging.Pageable"--%>

<c:forEach var="item" items="${pageable.items}" varStatus="status">
  <hst:link var="link" hippobean="${item}"/>
  <article class="has-edit-button">
    <hst:cmseditlink hippobean="${item}"/>
    <h3><a href="${link}"><c:out value="${item.title}"/></a></h3>
    <c:if test="${hst:isReadable(item, 'date.time')}">
      <p>
        <fmt:formatDate value="${item.date.time}" type="both" dateStyle="medium" timeStyle="short"/>
      </p>
    </c:if>
    <p><c:out value="${item.introduction}"/></p>
  </article>
</c:forEach>
<c:if test="${pageable.showPagination}">
  <%@ include file="/WEB-INF/jsp/include/pagination.jsp" %>
</c:if>
<%--@elvariable id="editMode" type="java.lang.Boolean"--%>
<c:if test="${editMode and empty pageable}">
  <img src="<hst:link path='/images/essentials/catalog-component-icons/news-list.png'/>"> Click to edit News List
</c:if>