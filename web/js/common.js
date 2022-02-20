function _change() {
	$("#vCode").attr("src", "/BookWeb/VerifyCodeServlet?a=" + new Date().getTime());
}