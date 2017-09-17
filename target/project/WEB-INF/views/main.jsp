<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<tiles:importAttribute name="angular"/>
<tiles:importAttribute name="controllers"/>
<tiles:importAttribute name="application"/>
<tiles:importAttribute name="services"/>

<html>
  <head>
    <title tiles:fragment="title">Main</title>
  </head>
  <body ng-app="mainApp">

    <c:forEach var="angular" items="${angular}">
        <script src="<c:url value="${angular}"/>" type="text/javascript"></script>
    </c:forEach>

    jstl not working yet -> otherwise this should go to angular

    <c:forEach var="application" items="${application}">
        <script src="<c:url value="${application}"/>" type="text/javascript"></script>
    </c:forEach>

    <c:forEach var="controller" items="${controllers}">
        <script src="<c:url value="${controller}"/>" type="text/javascript"></script>
    </c:forEach>

    <c:forEach var="services" items="${services}">
        <script src="<c:url value="${services}"/>" type="text/javascript"></script>
    </c:forEach>


  <ng-view></ng-view>
  </body>
</html>
