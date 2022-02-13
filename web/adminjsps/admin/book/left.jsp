<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'left.jsp' starting page</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="<c:url value='/adminjsps/admin/css/book/left.css'/>">
	<script type="text/javascript" src="<c:url value='/menu/mymenu.js'/>"></script>
	<link rel="stylesheet" href="<c:url value='/menu/mymenu.css'/>" type="text/css" media="all">
<script language="javascript">
var bar = new Q6MenuBar("bar", "图书分类");

function load() {
	bar.colorStyle = 2;
	bar.config.imgDir = "<c:url value='/menu/img/'/>";
	bar.config.radioButton=true;

	bar.add("程序设计", "Java Javascript", "<c:url value='/adminjsps/admin/book/list.jsp'/>", "body");
	bar.add("程序设计", "JSP", "<c:url value='/adminjsps/admin/book/list.jsp'/>", "body");
	bar.add("程序设计", "C C++ VC VC++", "<c:url value='/adminjsps/admin/book/list.jsp'/>", "body");

	bar.add("办公室用书", "微软Office", "<c:url value='/adminjsps/admin/book/list.jsp'/>", "body");
	bar.add("办公室用书", "计算机初级入门", "<c:url value='/adminjsps/admin/book/list.jsp'/>", "body");

	bar.add("图形 图像 多媒体", "Photoshop", "<c:url value='/adminjsps/admin/book/list.jsp'/>", "body");
	bar.add("图形 图像 多媒体", "3DS MAX", "<c:url value='/adminjsps/admin/book/list.jsp'/>", "body");
	bar.add("图形 图像 多媒体", "网页设计", "<c:url value='/adminjsps/admin/book/list.jsp'/>", "body");
	bar.add("图形 图像 多媒体", "Flush", "<c:url value='/adminjsps/admin/book/list.jsp'/>", "body");

	bar.add("操作系统/系统开发", "Windows", "<c:url value='/adminjsps/admin/book/list.jsp'/>", "body");
	bar.add("操作系统/系统开发", "Linux", "<c:url value='/adminjsps/admin/book/list.jsp'/>", "body");
	bar.add("操作系统/系统开发", "系统开发", "<c:url value='/adminjsps/admin/book/list.jsp'/>", "body");

	var d = document.getElementById("menu");
	d.innerHTML = bar.toString();
}
</script>
  </head>

  <body onload="load()">
<div id="menu"></div>
  </body>
</html>
