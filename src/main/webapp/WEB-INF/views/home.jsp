<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

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
						<td align="left"><c:out value="${user.screenName}" /></td>
					</tr>
				</c:forEach>
			</table>
			<div>
				<form name="twitter" action="login-twitter">
					<input type="submit" value="Twitter" />
				</form>
				<form name="facebook" action="login-facebook">
					<input type="submit" value="Facebook" />
				</form>
			</div>

		</div>
		<div id="content" class="content">
			<h3>Statuses:</h3>
			<table>
				<c:forEach items="${home.statuses}" var="status">
					<tr>
						<td align="left"><c:out value="${status.user.screenName}" /></td>
						<td align="left"><c:out value="${status.publishedAt}" /></td>
						<td align="left"><c:out value="${status.text}" /></td>
				</c:forEach>
			</table>
		</div>
	</div>

</body>
</html>
