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
                normal: true,
                filtered: true
            }
        };
    },
    handleSubmit: function(dict) {
        var text = jQuery(this.refs['#input'].getDOMNode()).text();
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
    toNormal: function(e) {
        e.preventDefault();
        e.stopPropagation();
        this.handleSubmit("normal");
    },
    toFiltered: function(e) {
        e.preventDefault();
        e.stopPropagation();
        this.handleSubmit("filtered");
    },
    render: function() {
        return (<section className="container" style={{minHeight: "300px"}}>
                <ul className="nav nav-tabs nav-justified">
                <li className="active">
                <a href="#input" data-toggle="tab">
                Your text
                </a>
                </li>

                <li>
                <a href="#normal" onClick={this.toNormal} data-toggle="tab">
                Normal analysis
                </a>
                </li>
                
                <li>
                <a href="#filtered" onClick={this.toFiltered} data-toggle="tab">
                Filtered analysis
                </a>
                </li>
                </ul>
                
                <section className="tab-content">
                <InputPane ref="#input"/>
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
                className="tab-pane active"
                id="input"
                contentEditable="true"
                style={{minHeight: "200px"}}>
                this, and that!
                </section>);
    }
});


var OutputPane = React.createClass({
    getInitialState: function() { return { curr: undefined }; },
    handleShowPopover: function(e) {
        if (this.state.curr !== undefined)  $(this.state.curr).popover('hide');
        this.setState({ curr: e.target });
    },
    handleHidePopover: function(e) {
        this.setState({popoverShown: undefined});
    },
    componentDidMount: function() {
        var DOMNode = this.getDOMNode();
        jQuery(DOMNode).popover({
            selector: '[rel="popover"]:visible',
            html: true,
            content: '<div>hai!</div>'
        });
        jQuery(DOMNode).on('show.bs.popover', this.handleShowPopover);
        jQuery(DOMNode).on('hide.bs.popover', this.handleHidePopover);
    },
    componentWillUnmount: function() {
        var DOMNode = this.getDOMNode();
        jQuery(DOMNode).off('show.bs.popover');
        jQuery(DOMNode).off('hide.bs.popover');
        jQuery(DOMNode).popover('destroy');
    },
    render: function() {
        return (<section className="tab-content">
                {this.props.children}
                </section>            
        );
    },
    byBegins: function() {
        var byBegins = {}, byEnds = {}, lastBegin = undefined;
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
        var result = [], pairs = _.sortBy(_.pairs(byBegins),
                                          function(p) { return Number(p[0]); });
        _.each(pairs, function(p) {
            p[1].sort(function(a, b) {
                return _.last(a.original.tokens).end -
                    _.last(b.original.tokens).end;
            });
        });
        _.each(pairs, function(p, i) {
            var begin = +p[0], pEnd = begin, anns = p[1], next = undefined;
            if (pairs[i + 1] !== undefined) {
                next = pairs[i + 1][0];
            }
            for(var j = 0; j < anns.length; j++) {
                var ann = anns[j], end = _.last(ann.original.tokens).end;
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
        var txt = this.props.data.text, pEnd = 0, f = true, output = [];
        _.each(map, function(m, i) {
            var begin = m[0], end = m[1], anns = m[2];
            if (pEnd !== begin) {
                output.push(txt.substring(pEnd, begin));
            }
            output.push(<Mapping style={f ? 'red' : 'blue'} data={anns}
                        text={txt.substring(begin, end)} ref={begin}/>);
            pEnd = end;
            f = !f;
        });
        output.push(txt.substring(pEnd, txt.length));
        return output;
    },
    render: function() {
        return (<section id={this.props.id} className="tab-pane">
                {this.toHtml(this.byBeginsAndEnds(this.byBegins()))}</section>);
    }
});

var Mapping = React.createClass({
    shown: undefined,
    getInitialState: function()  {
        return  {
            htmlContents: {
            }
        }
    },
    render: function() {
        return (<span
                style={{color: this.props.style}}
                rel="popover"
                data-title={this.props.text}
                data-html-content-ref={this.props.ref}
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
