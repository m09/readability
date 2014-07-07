/** @jsx React.DOM */
var Annotator = React.createClass({
  getInitialState: function() {
    return {
      data: {
        noisy: {
          scores: [],
          monoids: [],
          text: "",
          tokens: [],
          revisions: [],
          annotations: [],
          rewritings: []
        },
        filtered: {
          scores: [],
          monoids: [],
          text: "",
          tokens: [],
          revisions: [],
          annotations: [],
          rewritings: []
        }
      },
      weight: 0,
      monoid: 0,
      corpus: 'filtered',
      tab: 'input',
      lastText: "",
      fetched: { noisy: true, filtered: true }
    };
  },
  fetchData: function(corpus) {
    var text = jQuery("#input").val();
    if (text === this.state.lastText) {
      if (this.state.fetched[corpus]) return;
    } else this.setState({ fetched: { noisy: false, filtered: false } });
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
      data: JSON.stringify({ data: text, dict: corpus })
    }).done(function(overlay, data) {
      var dataMergeable = jQuery.extend({}, this.state.data);
      dataMergeable[corpus] = data;
      var fetchedMergeable = jQuery.extend({}, this.state.fetched);
      fetchedMergeable[corpus] = true;
      this.setState( { data: dataMergeable, fetched: fetchedMergeable } );
      overlay.remove();
    }.bind(this, overlay));
  },
  controlCallbackWeight: function(e) {
    this.setState({weight: e.target.options.selectedIndex});
  },
  controlCallbackCorpus: function(e) {
    var index = e.target.options.selectedIndex;
    var corpus = e.target.options[index].value;
    this.setState({corpus: corpus});
    if (this.state.tab === 'analysis'
        || this.state.tab === 'rewritings') {
      this.fetchData(corpus);
    }
  },
  controlCallbackMonoid: function(e) {
    this.setState({monoid: e.target.options.selectedIndex});
  },
  activateOutputTab: function(tab) {
    this.fetchData(this.state.corpus);
    this.activateTab(tab);
  },
  activateTab: function(tab) {
    this.setState({tab: tab});
  },
  render: function() {
    return (<section ref="container" className="container" style={{minHeight: "300px"}}>
            <ControlPane
            weight={this.state.weight}
            corpus={this.state.corpus}
            scores={this.state.data[this.state.corpus].scores}
            monoids={this.state.data[this.state.corpus].monoids}
            callbackWeight={this.controlCallbackWeight}
            callbackCorpus={this.controlCallbackCorpus}
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
            Analysis
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
            data={this.state.corpus === 'noisy'
                  ? this.state.data.noisy
                  : this.state.data.filtered}
            active={this.state.tab === 'analysis'
                    ? true
                    : false}/>
            <RewritingsPane id="rewritings"
            weight={this.state.weight}
            monoid={this.state.monoid}
            data={this.state.corpus === 'noisy'
                  ? this.state.data.noisy
                  : this.state.data.filtered}
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
