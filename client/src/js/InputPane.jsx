/** @jsx React.DOM */
var InputPane = React.createClass({
  render: function() {
    return (<section className={this.props.active
                                ? "tab-pane active"
                                : "tab-pane"}
                     style={{height: "300px"}}>
            <textarea id="input"
                      style={{width: "100%",
                              height: "100%"}}>
            When the US National Security Agency (NSA) intercepted the online accounts of legally targeted foreigners over a four-year period it also collected the conversations of nine times as many ordinary internet users, both Americans and non-Americans, according to an investigation by the Washington Post.


            Nearly half of those surveillance files contained names, email addresses or other details that the NSA marked as belonging to US citizens or residents, the Post reported in a story posted on its website on Saturday night. While the federal agency tried to protect their privacy by masking more than 65,000 such references to individuals, the newspaper said it found nearly 900 additional email addresses that could be strongly linked to US citizens or residents.


            The intercepted messages contained material of considerable intelligence value, the Post reported, such as information about a secret overseas nuclear project, double-dealing by an ostensible ally, a military calamity that befell an unfriendly power and the identities of aggressive intruders into US computer networks.</textarea>
            </section>);
  }
});
