/*
 * Paintroid: An image manipulation application for Android.
 * Copyright (C) 2010-2021 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.paintroid.command.serialization

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import org.catrobat.paintroid.command.Command
import org.catrobat.paintroid.model.CommandManagerModel

class CommandManagerModelSerializer(version: Int) : VersionSerializer<CommandManagerModel>(version) {
    override fun write(kryo: Kryo, output: Output, model: CommandManagerModel) {
        with(kryo) {
            writeClassAndObject(output, model.initialCommand)
            output.writeInt(model.commands.size)
            model.commands.forEach { command ->
                writeClassAndObject(output, command)
            }
        }
    }

    override fun read(kryo: Kryo, input: Input, type: Class<out CommandManagerModel>): CommandManagerModel =
        super.handleVersions(this, kryo, input, type)

    override fun readCurrentVersion(kryo: Kryo, input: Input, type: Class<out CommandManagerModel>): CommandManagerModel {
        return with(kryo) {
            val initCommand = kryo.readClassAndObject(input) as Command
            val size = input.readInt()
            val commandList = ArrayList<Command>()
            repeat(size) {
                commandList.add(kryo.readClassAndObject(input) as Command)
            }
            CommandManagerModel(initCommand, commandList)
        }
    }
}
