/** @jsx React.DOM */
var Annotator = React.createClass({
    getInitialState: function() {
        return {
            data: {
                normal: { text: "", tokens: [], annotations: [] },
                filtered: { text: "", tokens: [], annotations: [] }
            },
            dict: jQuery("input:radio[name='dict']:checked").val(),
            lastText: "",
            fetched: { normal: true, filtered: true }
        };
    },
    fetchAnnotations: function(dict) {
        var text = jQuery(this.refs.input.getDOMNode()).text();
        if (text === this.state.lastText) {
            if (this.state.fetched[dict]) return;
        } else this.setState({ fetched: { normal: false, filtered: false } });
        this.setState({lastText: text});
        jQuery.ajax({
            type: 'POST',
            url: this.props.url,
            contentType: 'application/json; charset=UTF-8',
            data: JSON.stringify({ data: text, dict: dict })
        }).done(function(data) {
            var dataMergeable = jQuery.extend({}, this.state.data);
            dataMergeable[dict] = data;
            var fetchedMergeable = jQuery.extend({}, this.state.fetched);
            fetchedMergeable[dict] = true;
            this.setState({ data: dataMergeable, fetched: fetchedMergeable });
        }.bind(this));
    },
    toNormal: function(e) { this.fetchAnnotations("normal"); },
    toFiltered: function(e) { this.fetchAnnotations("filtered"); },
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
                <InputPane ref="input"/>
                <OutputPane id="normal" data={this.state.data.normal}/>
                <OutputPane id="filtered" data={this.state.data.filtered}/>
                </section>
                </section>
        );
    }
});
