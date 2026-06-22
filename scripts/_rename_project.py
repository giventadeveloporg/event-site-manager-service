#!/usr/bin/env python3
"""One-shot rename: event-site-manager-service -> event-site-manager-service."""
from __future__ import annotations

import os
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SKIP_DIRS = {".git", "target", "node_modules", ".idea", "dist", "build", "infrastructure_deployment"}

REPLACEMENTS = [
    ("com.eventsitemanager", "com.eventsitemanager"),
    ("EventSiteManagerServiceApp", "EventSiteManagerServiceApp"),
    ("eventSiteManagerServiceApp", "eventSiteManagerServiceApp"),
    ("event-site-manager-service", "event-site-manager-service"),
    ("event_site_manager_service", "event_site_manager_service"),
    ("Event Site Manager Service", "Event Site Manager Service"),
    ("event-site-manager-service", "event-site-manager-service"),
    ("event_site_manager_db", "event_site_manager_db"),
    ("giventadeveloporg/event-site-manager-service", "giventadeveloporg/event-site-manager-service"),
    ("Event Site Manager Service", "Event Site Manager Service"),
]

TEXT_EXTENSIONS = {
    ".java", ".xml", ".yml", ".yaml", ".json", ".md", ".mdc", ".properties",
    ".html", ".js", ".sh", ".bat", ".ps1", ".py", ".txt", ".cursorrules",
}


def should_process(path: Path) -> bool:
    if any(part in SKIP_DIRS for part in path.parts):
        return False
    if path.name in {"Dockerfile", "Procfile"}:
        return True
    return path.suffix.lower() in TEXT_EXTENSIONS


def main() -> None:
    changed = 0
    for dirpath, dirnames, filenames in os.walk(ROOT):
        dirnames[:] = [d for d in dirnames if d not in SKIP_DIRS]
        for name in filenames:
            path = Path(dirpath) / name
            if not should_process(path):
                continue
            try:
                text = path.read_text(encoding="utf-8")
            except (UnicodeDecodeError, OSError):
                continue
            original = text
            for old, new in REPLACEMENTS:
                text = text.replace(old, new)
            if text != original:
                path.write_text(text, encoding="utf-8", newline="\n")
                changed += 1
    print(f"Updated {changed} files")


if __name__ == "__main__":
    main()
