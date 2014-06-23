/** @jsx React.DOM */
var Annotator = React.createClass({
    getInitialState: function() {
        return {
            data: {
                noisy: {
                    text: "",
                    tokens: [],
                    revisions: {
                        text: [],
                        pos: []
                    },
                    annotations: {
                        text: [],
                        pos: []
                    },
                    rewritings: []
                },
                filtered: {
                    text: "",
                    tokens: [],
                    revisions: {
                        text: [],
                        pos: []
                    },
                    annotations: {
                        text: [],
                        pos: []
                    },
                    rewritings: []
                }
            },
            weight: 0,
            corpus: 'filtered',
            tab: 'input',
            lastText: "",
            fetched: { noisy: true, filtered: true }
        };
    },
    fetchData: function(corpus) {
        var text = jQuery(this.refs.input.getDOMNode()).text();
        if (text === this.state.lastText) {
            if (this.state.fetched[corpus]) return;
        } else this.setState({ fetched: { noisy: false, filtered: false } });
        this.setState({lastText: text});
        jQuery.ajax({
            type: 'POST',
            url: this.props.url,
            contentType: 'application/json; charset=UTF-8',
            data: JSON.stringify({ data: text, dict: corpus })
        }).done(function(data) {
            var dataMergeable = jQuery.extend({}, this.state.data);
            dataMergeable[corpus] = data;
            var fetchedMergeable = jQuery.extend({}, this.state.fetched);
            fetchedMergeable[corpus] = true;
            this.setState({ data: dataMergeable, fetched: fetchedMergeable });
        }.bind(this));
    },
    controlCallbackWeight: function(weight) {
        this.setState({weight: weight});
    },
    controlCallbackCorpus: function(corpus) {
        this.setState({corpus: corpus});
        if (this.state.tab === 'analysis'
            || this.state.tab === 'rewritings') {
            this.fetchData(corpus);
        }
    },
    activateOutputTab: function(tab) {
        this.fetchData(this.state.corpus);
        this.activateTab(tab);
    },
    activateTab: function(tab) {
        this.setState({tab: tab});
    },
    render: function() {
        return (<section className="container" style={{minHeight: "300px"}}>
                <ControlPane
                weight={this.state.weight}
                corpus={this.state.corpus}
                callbackWeight={this.controlCallbackWeight}
                callbackCorpus={this.controlCallbackCorpus}/>
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
                <InputPane ref="input"
                           active={this.state.tab === 'input'
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
