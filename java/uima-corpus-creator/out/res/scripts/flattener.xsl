<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output method="text" version="1.0" encoding="UTF-8"/>

  <xsl:strip-space elements="*"/>

  <xsl:param name="score"/>

  <xsl:template match="/">
    <xsl:apply-templates select="dict/original"/>
  </xsl:template>

  <xsl:template match="original">
    <xsl:variable name="originalText">
      <xsl:value-of select="text/text()"/>
    </xsl:variable>
    <xsl:for-each select="revised-list/revised">
      <xsl:value-of select="@*[local-name()=concat('score', $score)]"/>
      <xsl:text>	</xsl:text>
      <xsl:value-of select="$originalText"/>
      <xsl:text>	</xsl:text>
      <xsl:value-of select="text"/>
      <xsl:text>&#xa;</xsl:text>
    </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>
