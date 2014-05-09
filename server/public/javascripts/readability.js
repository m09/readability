/** @jsx React.DOM */
var Annotator = React.createClass({
    getInitialState: function() {
        return {
            data: {
                normal: {
                    text: "",
                    annotations: []
                },
                filtered: {
                    text: "",
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
        return (
                <section className="active tab-pane" id="input" contentEditable="true" style={{minHeight: "200px"}}>Hai there!</section>
        );
    }
});

var OutputPane = React.createClass({
    render: function() {
        var mappingNodes = this.props.data.annotations.map(function (mapping) {
            return <Mapping mapping={mapping}></Mapping>;
        });
        return (
            <section id={this.props.id} className="tab-pane">
                <ul>{mappingNodes}</ul>
            </section>
        );
    }
});

var Mapping = React.createClass({
    render: function() {
        var revisionNodes = this.props.mapping.revised.map(function (revision) {
            return <li>{revision.text}</li>;
        });
        return (
                <li>{this.props.mapping.original.text}:
                <ul>{revisionNodes}</ul>
                </li>
        );
    }
});

React.renderComponent(
        <Annotator url="http://localhost:9000/"/>,
    document.getElementById('annotator')
);
