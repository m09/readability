/** @jsx React.DOM */
var InputPane = React.createClass({
  render: function() {
    return (<section className={this.props.active
                                ? "tab-pane active"
                                : "tab-pane"}
                     style={{height: "300px"}}>
            <textarea id="input"
                      style={{width: "100%",
                              height: "100%"}}>The family of Hafez Hamad, a senior member of Islamic Jihad, were sitting on a pair of low orange divans in the space between two houses when the rocket hit them a little before midnight.

Fired from a drone it slammed into the ground a foot from one of the two sofas, leaving behind a round three-feet deep hole and five people dead, including Hafez and his 20-year-old niece. "They were just talking, sitting outside their house," says Mariam Hamad, sister-in-law of Hafez. "Usually there is a warning, but in this case the missile struck out of the blue."

She meant a practice known as the "knock on the roof" – when small projectiles are fired to warn civilians to leave buildings. In other cases strikes have been preceded by a telephone call telling its inhabitants to flee. But such bombing sometimes injures or kills people in neighbouring houses. In any case there was no knock on the roof for the Hamad family.

Even this early in Israel's campaign against Hamas and other militant factions in Gaza the bodies of the civilian victims are beginning to pile up, children and an 80-year-old woman among the dead from the past two days.

As the Israeli military said it struck about 200 Hamas targets on the second day of its offensive and warned of a possible ground invasion, the rockets militants from Gaza continued to fire were intermittently visible being launched in pairs, threes and even sets of four, their vapour trails climbing into the Mediterranean sky. The Israeli military said more than 60 rockets were fired at Israel on Wednesday, forcing people to take cover in public shelters as far away as Jerusalem. So far there have been no fatalities.</textarea>
            </section>);
  }
});
