<%
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setHeader("Expires","0");
%>

<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="ctx" value="${pageContext['request'].contextPath}"/>

<!DOCTYPE HTML>
<html>
<head>
<title>Viewer</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
</head>
<body>
	<div class="container">
		<div id="header" class="header">
			<h3>Users:</h3>
			<table>
				<c:forEach items="${home.users}" var="user">
					<tr>
						<td align="left"><c:out value="${user.serviceName}" /></td>
						<td align="left"><c:out value="${user.displayName}" /></td>
					</tr>
				</c:forEach>
			</table>
			<div>
				<c:choose>
					<c:when test="${(twitter == null) || (twitter.connection == null)}">
						<form name="twitter" action="twitter/signin">
							<input type="submit" value="Connect Twitter" />
						</form>
					</c:when>
					<c:otherwise>
						<form name="twitter" action="twitter/logout">
							<input type="submit" value="Disconnect Twitter" />
						</form>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${(facebook == null) || (facebook.connection == null)}">
						<form name="twitter" action="facebook/signin">
							<input type="submit" value="Connect Facebook" />
						</form>
					</c:when>
					<c:otherwise>
						<form name="facebook" action="facebook/logout">
							<input type="submit" value="Disconnect Facebook" />
						</form>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div id="content" class="content">
			<h3>Statuses:</h3>
			<table border="1">
				<c:forEach items="${home.statuses}" var="status">
					<tr>
						<td align="left"><c:out value="${status.user.serviceName}" /></td>
						<td align="left"><c:out value="${status.user.displayName}" /></td>
						<td align="left"><c:out value="${status.publishedAt}" /></td>
						<td align="left"><c:out value="${status.text}" /></td>
				</c:forEach>
			</table>
		</div>
	</div>

</body>
</html>
