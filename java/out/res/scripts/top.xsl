<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output method="text"/>

  <xsl:strip-space elements="*"/>

  <xsl:template match="/">
      <xsl:apply-templates select="dict/original">
        <xsl:sort select="count( revised-list/revised )"
                  order="descending"
                  data-type="number"
                  />
      </xsl:apply-templates>
  </xsl:template>

  <xsl:template match="original">
    <xsl:value-of select="count( revised-list/revised )"/>
    <xsl:text>	</xsl:text>
    <xsl:value-of select="text/text()"/>
    <xsl:text>
</xsl:text>
  </xsl:template>
</xsl:stylesheet>
