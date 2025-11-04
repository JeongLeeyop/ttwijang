Add-Type -AssemblyName System.Drawing

# Create a 32x32 bitmap
$bmp = New-Object System.Drawing.Bitmap(32, 32)
$g = [System.Drawing.Graphics]::FromImage($bmp)
$g.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::AntiAlias

# Draw blue circle background
$blueBrush = New-Object System.Drawing.SolidBrush([System.Drawing.Color]::FromArgb(6, 29, 161))
$g.FillEllipse($blueBrush, 1, 1, 30, 30)

# Draw white soccer ball pattern
$whiteBrush = New-Object System.Drawing.SolidBrush([System.Drawing.Color]::FromArgb(230, 255, 255, 255))

# Pentagon patterns
$pentagons = @(
    @(@(16,4), @(19,10), @(17,13), @(15,13), @(13,10)),
    @(@(22,9), @(26,12), @(25,16), @(21,14), @(20,11)),
    @(@(27,16), @(27,20), @(23,21), @(22,18), @(24,16)),
    @(@(25,23), @(22,27), @(18,25), @(19,22), @(23,22)),
    @(@(16,27), @(12,26), @(12,23), @(15,23), @(17,25)),
    @(@(7,23), @(6,19), @(9,18), @(12,20), @(11,23)),
    @(@(5,16), @(6,12), @(10,13), @(10,16), @(7,17)),
    @(@(7,9), @(11,7), @(14,10), @(12,13), @(8,12))
)

foreach($pentagon in $pentagons) {
    $points = $pentagon | ForEach-Object { 
        New-Object System.Drawing.Point $_[0], $_[1] 
    }
    $g.FillPolygon($whiteBrush, $points)
}

# Save as PNG first
$pngPath = Join-Path $PSScriptRoot "favicon-temp.png"
$bmp.Save($pngPath, [System.Drawing.Imaging.ImageFormat]::Png)

# Convert to ICO
$icon = [System.Drawing.Icon]::FromHandle($bmp.GetHicon())
$icoPath = Join-Path $PSScriptRoot "favicon.ico"
$stream = New-Object System.IO.FileStream($icoPath, [System.IO.FileMode]::Create)
$icon.Save($stream)
$stream.Close()

# Cleanup
$g.Dispose()
$bmp.Dispose()
$blueBrush.Dispose()
$whiteBrush.Dispose()

Write-Host "Favicon created successfully at $icoPath"
