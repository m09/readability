<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>

  <xsl:strip-space elements="*"/>

  <xsl:template match="/">
    <dict>
      <xsl:apply-templates select="dict/original">
        <xsl:sort select="count( revised-list/revised )"
                  order="descending"
                  data-type="number"
                  />
      </xsl:apply-templates>
    </dict>
  </xsl:template>

  <xsl:template match="original">
    <xsl:copy>
      <xsl:apply-templates select="node()|@*"/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="node()|@*">
    <xsl:copy>
      <xsl:apply-templates select="node()|@*"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
