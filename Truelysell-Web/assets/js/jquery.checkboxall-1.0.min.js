/*!
 * jquery.checkboxall - Turn on/off all checkbox in the container.
 *
 * @version v1.0
 * @homepage http://neeed.us/
 * @demo: http://neeed.us/plugins/jquery.checboxall/
 * @author Norbert Bracsok <norbert@neeed.us>
 * Licensed under the MIT license
 */!function(e){"use strict";if("undefined"==typeof jQuery)return console.log("jquery.checkboxall plugin needs the jquery plugin"),!1;e.fn.checkboxall=function(c){void 0===c&&(c="all");var o=this;if(e("."+c,o).length){var l=e("."+c,o),n=o.find('input[type="checkbox"]'),t=n.not("."+c,o);return n.unbind("click").click(function(o){o.stopPropagation();var r=e(this);r.hasClass(c)?n.prop("checked",r.prop("checked")):t.length!==t.filter(":checked").length?l.prop("checked",!1):l.prop("checked",!0)})}console.log("jquery.checkboxall error: main selector is not exists."),console.log("Please add 'all' class to first checkbox or give the first checkbox a class name and enter the checkboxall() functions for the class name!"),console.log("Example: $(selector).checkboxall('your-checkbox-class-name');")}}(jQuery);