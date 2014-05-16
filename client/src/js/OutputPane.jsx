/** @jsx React.DOM */
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
            html: true
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