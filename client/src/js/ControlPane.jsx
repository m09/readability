/** @jsx React.DOM */
var ControlPane = React.createClass({
  render: function() {
    return (
        <section className="settings">
        <div className="row">
        <div className="col-sm-2">
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
        <div className="col-sm-6">
        Weight:&nbsp;
        <div className="btn-group btn-group-xs">
        <button className={this.props.weight === 0
                           ? 'btn btn-default active'
                           : 'btn btn-default'}
      onClick={this.props.callbackWeight.bind(this, 0)}>
        Occ
      </button>
        <button className={this.props.weight === 1
                           ? 'btn btn-default active'
                           : 'btn btn-default'}
      onClick={this.props.callbackWeight.bind(this, 1)}>
        LM
      </button>
        <button className={this.props.weight === 2
                           ? 'btn btn-default active'
                           : 'btn btn-default'}
      onClick={this.props.callbackWeight.bind(this, 2)}>
        LMN
      </button>
        <button className={this.props.weight === 3
                           ? 'btn btn-default active'
                           : 'btn btn-default'}
      onClick={this.props.callbackWeight.bind(this, 3)}>
        LMW
      </button>
        <button className={this.props.weight === 4
                           ? 'btn btn-default active'
                           : 'btn btn-default'}
      onClick={this.props.callbackWeight.bind(this, 4)}>
        LMWN
      </button>
        </div>
        </div>
        </div>
        </section>
    );
  }
});
