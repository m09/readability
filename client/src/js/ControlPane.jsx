/** @jsx React.DOM */
var ControlPane = React.createClass({
    render: function() {
        return (
          <section className="settings">
          <div className="row">
            <div className="col-sm-4">
            Settings:
            </div>
            <div className="col-sm-4">
              Corpus:&nbsp;
              <div className="btn-group btn-group-xs">
                <button className={this.props.corpus === 'filtered'
                                     ? 'btn btn-default active'
                                     : 'btn btn-default'}
                    onClick={this.props.callbackCorpus.bind(null, 'filtered')}>
                  Filtered
                </button>
                <button className={this.props.corpus === 'noisy'
                                     ? 'btn btn-default active'
                                     : 'btn btn-default'}
                    onClick={this.props.callbackCorpus.bind(null, 'noisy')}>
                  Noisy
                </button>
              </div>
            </div>
            <div className="col-sm-4">
              Weight:&nbsp;
              <div className="btn-group btn-group-xs">
                <button className={this.props.weight === 'a'
                                     ? 'btn btn-default active'
                                     : 'btn btn-default'}
                    onClick={this.props.callbackWeight.bind(this, 'a')}>
                  a
                </button>
                <button className={this.props.weight === 'b'
                                     ? 'btn btn-default active'
                                     : 'btn btn-default'}
                    onClick={this.props.callbackWeight.bind(this, 'b')}>
                  b
                </button>
                <button className={this.props.weight === 'c'
                                     ? 'btn btn-default active'
                                     : 'btn btn-default'}
                    onClick={this.props.callbackWeight.bind(this, 'c')}>
                  c
                </button>
              </div>
            </div>
          </div>
          </section>
        );
    }
});
