/** @jsx React.DOM */
var Annotator = React.createClass({
  getInitialState: function() {
    return {
      data: {
        scores: [],
        monoids: [],
        text: "",
        tokens: [],
        revisions: [],
        annotations: [],
        rewritings: []
      },
      weight: 0,
      monoid: 0,
      tab: 'input',
      lastText: "",
      fetched: true
    };
  },
  fetchData: function() {
    var text = jQuery("#input").val();
    if (text === this.state.lastText) {
      if (this.state.fetched) return;
    } else this.setState({ fetched: false });
    this.setState({lastText: text});
    var overlay = jQuery(
      '<div id="overlay">' +
        '<div id="loading">' +
        'The first request might take ~2 minutes ' +
        'to complete: the application is shut down after ' +
        '1h of inactivity and may have to be restarted. ' +
        'Subsequent ones should be faster.' +
        '<br>' +
        '<img src="img/ajax-loader.gif">' +
        '</div>' +
        '</div>');
    overlay.appendTo(jQuery(this.refs.container.getDOMNode()));
    jQuery.ajax({
      type: 'POST',
      url: this.props.url,
      contentType: 'application/json; charset=UTF-8',
      data: JSON.stringify(text)
    }).done(function(overlay, data) {
      this.setState( { data: data, fetched: true } );
      overlay.remove();
    }.bind(this, overlay));
  },
  controlCallbackWeight: function(e) {
    this.setState({weight: e.target.options.selectedIndex});
  },
  controlCallbackMonoid: function(e) {
    this.setState({monoid: e.target.options.selectedIndex});
  },
  activateOutputTab: function(tab) {
    this.fetchData();
    this.activateTab(tab);
  },
  activateTab: function(tab) {
    this.setState({tab: tab});
  },
  render: function() {
    return (<section ref="container" className="container" style={{minHeight: "300px"}}>
            <ControlPane
            weight={this.state.weight}
            scores={this.state.data.scores}
            monoids={this.state.data.monoids}
            callbackWeight={this.controlCallbackWeight}
            callbackMonoid={this.controlCallbackMonoid}
            tab={this.state.tab}/>
            <ul className="nav nav-tabs nav-justified">
            <li className={this.state.tab === 'input' ? 'active' : ''}>
            <a href="#"
            onClick={this.activateTab.bind(this, 'input')}>
            Input text
            </a>
            </li>

            <li className={this.state.tab === 'analysis' ? 'active' : ''}>
            <a href="#"
            onClick={this.activateOutputTab.bind(this, 'analysis')}>
            Lexical enhancements
            </a>
            </li>
            
            <li className={this.state.tab === 'rewritings' ? 'active' : ''}>
            <a href="#"
            onClick={this.activateOutputTab.bind(this, 'rewritings')}>
            Rewritings
            </a>
            </li>
            </ul>
            
            <section className="tab-content">
            <InputPane active={this.state.tab === 'input'
                               ? true
                               : false}/>
            <OutputPane id="analysis"
            weight={this.state.weight}
            data={this.state.data}
            active={this.state.tab === 'analysis'
                    ? true
                    : false}/>
            <RewritingsPane id="rewritings"
            weight={this.state.weight}
            monoid={this.state.monoid}
            data={this.state.data}
            active={this.state.tab === 'rewritings'
                    ? true
                    : false}/>
            </section>
            </section>
           );
  }
});

React.renderComponent(<Annotator url="http://localhost:9000/"/>,
                      document.getElementById('annotator'));
