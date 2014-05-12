/** @jsx React.DOM */
var Annotator = React.createClass({
    getInitialState: function() {
        return {
            data: {
                normal: {
                    text: "",
                    tokens: [],
                    annotations: []
                },
                filtered: {
                    text: "",
                    tokens: [],
                    annotations: []
                }
            },
            dict: jQuery("input:radio[name='dict']:checked").val(),
            lastText: "",
            fetched: {
                "normal": true,
                "filtered": true
            },
            currentTab: "input"
        };
    },
    handleSubmit: function(dict) {
        var text = jQuery(this.refs.inputText.getDOMNode()).text();
        if (text === this.state.lastText) {
            if(this.state.fetched[dict]) {
                return;
            }
        } else {
            this.setState({
                fetched: {
                    normal: false,
                    filtered: false
                }
            });
        }
        this.setState({lastText: text});
        jQuery.ajax({
            type: 'POST',
            url: this.props.url,
            contentType: 'application/json; charset=UTF-8',
            data: JSON.stringify({
                data: text,
                dict: dict
            })
        }).done(function(data) {
            var dataMergeable = jQuery.extend({}, this.state.data);
            dataMergeable[dict] = data;
            var fetchedMergeable = jQuery.extend({}, this.state.fetched);
            fetchedMergeable[dict] = true;
            this.setState({
                data: dataMergeable,
                fetched: fetchedMergeable
            });
        }.bind(this));
    },
    switchToNormal: function(e) {
        e.preventDefault();
        this.handleSubmit("normal");
    },
    switchToFiltered: function(e) {
        e.preventDefault();
        this.handleSubmit("filtered");
    },
    render: function() {
        return (
                <section className="container" style={{minHeight: "300px"}}>
                <ul className="nav nav-tabs nav-justified">
                <li className="active"><a href="#input" data-toggle="tab">Your text</a></li>
                <li><a href="#normal" onClick={this.switchToNormal} data-toggle="tab">Normal analysis</a></li>
                <li><a href="#filtered" onClick={this.switchToFiltered} data-toggle="tab">Filtered analysis</a></li>
                </ul>
                <section className="tab-content">
                <InputPane ref="inputText"/>
                <OutputPane id="normal" data={this.state.data.normal}/>
                <OutputPane id="filtered" data={this.state.data.filtered}/>
                </section>
                </section>
        );
    }
});

var InputPane = React.createClass({
    render: function() {
        return (<section
                  className="active tab-pane"
                  id="input"
                  contentEditable="true"
                  style={{minHeight: "200px"}}>
                    this, and that!
                </section>);
    }
});


var OutputPane = React.createClass({
    byBegins: function() {
        var byBegins = {};
        var byEnds = {};
        var lastBegin = undefined;
        _.each(this.props.data.annotations, function(ann) {
            var begin = _.first(ann.original.tokens).begin;
            var end = _.last(ann.original.tokens).end;
            var text = ann.original.text;
            if (byBegins[begin] === undefined) {
                byBegins[begin] = [];
                _.each(byEnds, function(anns, oEnd) {
                    if (oEnd > begin) {
                        byBegins[begin] = byBegins[begin].concat(anns);
                    }
                });
            }
            byBegins[begin].push(ann);
            byEnds[end] = byEnds[end] || [];
            byEnds[end].push(ann);
        });
        return byBegins;
    },
    byBeginsAndEnds: function(byBegins) {
        var result = [];
        var pairs = _.sortBy(_.pairs(byBegins), function(p) { return Number(p[0]); });
        _.each(pairs, function(p) {
            p[1].sort(function(a, b) {
                return _.last(a.original.tokens).end - _.last(b.original.tokens).end;
            });
        });
        _.each(pairs, function(p, i) {
            var begin = Number(p[0]);
            var anns = p[1]
            var next = undefined;
            if (pairs[i + 1] !== undefined) {
                next = pairs[i + 1][0];
            }
            var pEnd = begin;
            for(var j = 0; j < anns.length; j++) {
                var ann = anns[j];
                var end = _.last(ann.original.tokens).end;
                if (pEnd == end) {
                    break;
                }
                result.push([pEnd, Math.min(end, next || end), anns.slice(j)]);
                if (next !== undefined && next < pEnd) {
                    break;
                }
                pEnd = end;
            }
        });
        return result;
    },
    toHtml: function(map) {
        var wholeText = this.props.data.text;
        if (map.length === 0) {
            return wholeText;
        }
        var pEnd = 0;
        var f = true;
        var output = [];
        _.each(map, function(span, i) {
            var begin = span[0];
            var end = span[1];
            var anns = span[2];
            if (pEnd !== begin) {
                output.push(wholeText.substring(pEnd, begin));
            }
            output.push(<Mapping
                        style={f ? 'red' : 'blue'}
                        data={anns}
                        text={wholeText.substring(begin, end)}
                        />
            );
            pEnd = end;
            f = !f;
        });
        output.push(wholeText.substring(_.last(map)[1], wholeText.length));
        return output;
    },
    render: function() {
        return (
            <section id={this.props.id} className="tab-pane">
                {this.toHtml(this.byBeginsAndEnds(this.byBegins()))}
            </section>
        );
    }
});

var Mapping = React.createClass({
    shown: undefined,
    handleShowPopover: function(e) {
        if (this.shown !== undefined) {
            $(this.shown).popover('hide');
        }
        this.shown = e.target;
    },
    handleHidePopover: function(e) {
        if (this.shown === e.target) {
            this.shown = undefined;
        }
    },
    componentDidMount: function() {
        $(document).popover({
            selector: '[rel="popover"]:visible',
            html: true,
            content: '<div>hai!</div>'
        });
        $(document).on('show.bs.popover', this.handleShowPopover);
        $(document).on('hide.bs.popover', this.handleHidePopover);
    },
    componentWillUnmount: function() {
        $(document).off('show.bs.popover');
        $(document).off('hide.bs.popover');
        $(document).popover('destroy');
    },
    render: function() {
        return (<span
                style={{color: this.props.style}}
                rel="popover"
                data-title={this.props.text}
                >
                {this.props.text}
                </span>
        );
    }
});
React.renderComponent(
        <Annotator url="http://localhost:9000/"/>,
    document.getElementById('annotator')
);
