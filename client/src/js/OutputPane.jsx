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
    spans: function(text, anns) {
        var indices = [0, text.length];
        _.each(anns, function(ann) {
            var begin = _.first(ann.original.tokens).begin;
            var end = _.last(ann.original.tokens).end;
            indices.splice(_.sortedIndex(indices, begin), 0, begin);
            indices.splice(_.sortedIndex(indices, end), 0, end);
            indices = _.uniq(indices, true);
        });
        var previous = undefined;
        var result = [];
        _.each(indices, function(i) {
            if (previous !== undefined) {
                result.push([previous, i, []]);
            }
            previous = i;
        });
        return result;
    },
    fillSpans: function(anns, spans) {
        _.each(anns, function(ann) {
            _.each(spans, function(span) {
                var begin = _.first(ann.original.tokens).begin;
                var end = _.last(ann.original.tokens).end;
                if (begin < span[1] && end > span[0]) {
                    span[2].push(ann);
                }
            });
        });
    },
    toHtml: function(text, spans) {
        var output = [];
        var f = true;
        _.each(spans, function(span) {
            if (!_.isEmpty(span[2])) {
                output.push(<Mapping style={f ? 'red' : 'blue'} data={span[2]}
                            text={text.substring(span[0], span[1])}/>);
                f = !f;
            } else {
                output.push(text.substring(span[0], span[1]));
            }
        });
        return output;
    },
    render: function() {
        var text = this.props.data.text,
            anns = this.props.data.annotations.text,
            spans = this.spans(text, anns);
        this.fillSpans(anns, spans);
        var mappings = this.toHtml(text, spans);
        return (<section id={this.props.id} className="tab-pane">
                {mappings}</section>);
    }
});
