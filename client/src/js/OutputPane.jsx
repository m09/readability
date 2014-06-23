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
    spans: function(text, anns, revs) {
        var indices = [0, text.length];
        _.each(anns, function(ann) {
            indices.splice(_.sortedIndex(indices, ann.begin), 0, ann.begin);
            indices.splice(_.sortedIndex(indices, ann.end), 0, ann.end);
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
                if (ann.begin < span[1] && ann.end > span[0]) {
                    span[2].push(ann);
                }
            });
        });
    },
    toHtml: function(text, anns, spans, revs, scoreIndex) {
        var output = [];
        var f = true;
        var scores = _.pluck(_.pluck(_.flatten(_.values(revs)), 'score'), scoreIndex);
        var maxScore = _.max(scores);
        var minScore = _.min(scores);
        _.each(spans, function(span) {
            if (!_.isEmpty(span[2])) {
                output.push(<Mapping anns={span[2]}
                            revs={revs}
                            maxScore={maxScore}
                            minScore={minScore}
                            wholeText={text}
                            weight={scoreIndex}
                            text={text.substring(span[0], span[1])}/>);
                f = !f;
            } else {
                output.push(text.substring(span[0], span[1]));
            }
        }.bind(this));
        return output;
    },
    render: function() {
        var text = this.props.data.text,
            anns = this.props.data.annotations.text,
            revs = this.props.data.revisions.text,
            scoreIndex = this.props.weight
            spans = this.spans(text, anns, revs);
        this.fillSpans(anns, spans, revs);
        var mappings = this.toHtml(text, anns, spans, revs, scoreIndex);
        return (<section id={this.props.id}
                         className={this.props.active
                                      ? "tab-pane active"
                                      : "tab-pane"}>
                {mappings}</section>);
    }
});
