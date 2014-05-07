<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output method="text" version="1.0" encoding="UTF-8" indent="yes"/>

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
    <xsl:value-of select="count( revised-list/revised )"/>
    <xsl:text>	</xsl:text>
    <xsl:value-of select="text/text()"/>
    <xsl:text>&#xa;</xsl:text>
    <xsl:for-each select="revised-list/revised">
      <xsl:sort select="@count"
                order="descending"
                data-type="number"
                />
      <xsl:text>	</xsl:text>
      <xsl:value-of select="@count"/>
      <xsl:text>	</xsl:text>
      <xsl:value-of select="text"/>
    <xsl:text>&#xa;</xsl:text>
    </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>
