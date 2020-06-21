

#!/usr/bin/env python
import os
import zipfile
import sys

import shutil


def zipdir(path, ziph):    
    # ziph is zipfile handle
    for root, dirs, files in os.walk(path):
        for file in files:
            ziph.write(os.path.join(root, file))

if __name__ == '__main__':
    shutil.make_archive('desktop', 'zip', './TutorialQuest/desktop')
    shutil.make_archive('core.src', 'zip', './TutorialQuest/core/src')
    shutil.make_archive('core.assets', 'zip', './TutorialQuest/core/assets')

