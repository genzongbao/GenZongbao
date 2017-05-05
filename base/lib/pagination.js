function page(page, sender) {
	var form = jQuery(sender).parents('form');
	form.find("input[name=p]").val(page);
	form.submit();
}
