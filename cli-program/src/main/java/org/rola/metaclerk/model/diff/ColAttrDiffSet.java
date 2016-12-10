package org.rola.metaclerk.model.diff;

import java.util.HashSet;

class ColAttrDiffSet extends HashSet<ColumnDiffType> {
    @Override
    public boolean add(ColumnDiffType columnDiffType) {
        return super.add(columnDiffType);
    }
}
