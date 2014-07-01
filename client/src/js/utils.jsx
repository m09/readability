/** @jsx React.DOM */
// https://stackoverflow.com/questions/4535888
function htmlForTextWithEmbeddedNewlines(text) {
  var htmls = [];
  var lines = text.split(/\n/);
  // The temporary <div/> is to perform HTML entity encoding reliably.
  //
  // document.createElement() is *much* faster than jQuery('<div></div>')
  // https://stackoverflow.com/questions/268490
  //
  // You don't need jQuery but then you need to struggle with browser
  // differences in innerText/textContent yourself
  var tmpDiv = jQuery(document.createElement('div'));
  for (var i = 0 ; i < lines.length ; i++) {
    if (i > 0) {
      htmls.push(<br/>);
    }
    htmls.push(tmpDiv.text(lines[i]).html());
  }
  return htmls;
}
