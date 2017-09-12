<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<tiles:importAttribute name="angular"/>
<tiles:importAttribute name="angularModules"/>
<tiles:importAttribute name="angularComponents"/>
<tiles:importAttribute name="angularGlobal"/>
<tiles:importAttribute name="angularBootstrap"/>
<tiles:importAttribute name="nonAngularJS"/>
<tiles:importAttribute name="javascripts_custom"/>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="author" content="Petar Hariskov" />
    <meta name="company" content="FrameworkOnly" />
    <meta name="abstract" content="Connecting Problems & Solutions." />

    <title><tiles:insertAttribute name="title" ignore="true" /></title>

    <link rel="stylesheet" type="text/css" href="static/css/styles.css"/>

  </head>
  <body>

    <header class="hidden"></header>

    <ng-view> </ng-view>

    <rightpanel class="hidden"></rightpanel>
    <leftpanel class="hidden"></leftpanel>

    <c:forEach var="angular" items="${angular}">
        <script src="<c:url value="${angular}"/>"></script>
    </c:forEach>
    <c:forEach var="angularModules" items="${angularModules}">
        <script src="<c:url value="${angularModules}"/>"></script>
    </c:forEach>

    <c:forEach var="angularGlobal" items="${angularGlobal}">
        <script src="<c:url value="${angularGlobal}"/>"></script>
    </c:forEach>

    <c:forEach var="angularComponents" items="${angularComponents}">
        <script src="<c:url value="${angularComponents}"/>"></script>
    </c:forEach>

    <c:forEach var="angularBootstrap" items="${angularBootstrap}">
        <script src="<c:url value="${angularBootstrap}"/>"></script>
    </c:forEach>

    <c:forEach var="nonAngularJS" items="${nonAngularJS}">
        <script src="<c:url value="${nonAngularJS}"/>"></script>
    </c:forEach>

  </body>
</html>
